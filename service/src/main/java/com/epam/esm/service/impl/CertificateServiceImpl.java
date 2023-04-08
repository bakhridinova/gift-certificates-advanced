package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.mapper.CertificateMapper;
import com.epam.esm.util.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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
                .filter(t -> tagRepository.exists(t.getName()))
                .map(tagMapper::toTag)
                .collect(Collectors.toSet());

        searchFilter = searchFilter.inner(tags);

        return certificateRepository.findByFilter(searchFilter)
                .stream().map(certificateMapper::toCertificateDto).toList();
    }

    @Override
    public CertificateDto updateName(Long id, CertificateDto certificateDto) {
        Certificate certificate = certificateRepository.findById(id);
        certificate.setDescription(certificate.getDescription());
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateDto(certificate);
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        for (TagDto tagDto : certificateDto.getTags()) {
            String name = tagDto.getName();
            if (!tagRepository.exists(name)) {
                Tag tag = new Tag();
                tag.setName(name);
                tagRepository.save(tag);
            }
        }

        Certificate certificate = certificateMapper.toCertificate(certificateDto);
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateDto(certificate);
    }

    @Override
    public void delete(Long id) {
        certificateRepository.delete(certificateRepository.findById(id));
    }
}
