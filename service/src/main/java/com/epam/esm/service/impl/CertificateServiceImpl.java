package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import com.epam.esm.util.mapper.CertificateMapper;
import com.epam.esm.util.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final TagRepository tagRepository;
    private final CertificateMapper certificateMapper;
    private final TagMapper tagMapper;

    @Override
    public List<CertificateDto> findAll(Pagination pagination) {
        return certificateRepository.findAll(pagination).stream()
                .map(certificateMapper::toCertificateDto).toList();
    }

    @Override
    public CertificateDto findById(Long id) {
        return certificateMapper.toCertificateDto(certificateRepository.findById(id));
    }

    @Override
    public List<CertificateDto> findByFilter(SearchFilter searchFilter) {
        Set<Tag> tags = searchFilter.tagDtos().stream()
                .filter(t -> tagRepository.findByName(t.getName()).isPresent())
                .map(tagMapper::toTag)
                .collect(Collectors.toSet());

        searchFilter = searchFilter.inner(tags);

        return certificateRepository.findByFilter(searchFilter)
                .stream().map(certificateMapper::toCertificateDto).toList();
    }

    @Override
    public CertificateDto updateName(Long id, CertificateDto certificateDto) {
        Certificate certificate = certificateRepository.findById(id);
        certificate.setName(certificateDto.getName());
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        Set<Tag> tags = new TreeSet<>();
        for (TagDto tagDto : certificateDto.getTags()) {
            Optional<Tag> optionalTag = tagRepository.findByName(tagDto.getName());
            if (optionalTag.isPresent()) {
                tags.add(optionalTag.get());
            } else {
                Tag tag = tagMapper.toTag(tagDto);
                tagRepository.save(tag);
                tags.add(tag);
            }
        }
        Certificate certificate = certificateMapper.toCertificate(certificateDto);
        certificateRepository.save(certificate);
        tagRepository.setTags(certificate, tags);
        certificateDto = certificateMapper.toCertificateDto(certificate);
        certificateDto.setTags(tags.stream()
                .map(tagMapper::toTagDto).collect(Collectors.toSet()));
        return certificateDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        certificateRepository.delete(certificateRepository.findById(id));
    }
}
