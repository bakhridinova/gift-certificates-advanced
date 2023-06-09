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
    public List<CertificateDto> findAllByPage(int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return certificateRepository.findAllByPage(pagination).stream()
                .map(certificateMapper::toCertificateDto).toList();
    }

    @Override
    public CertificateDto findById(Long id) {
        return certificateMapper.toCertificateDto(certificateRepository.findById(id));
    }

    @Override
    public List<CertificateDto> findByFilterAndPage(SearchFilter searchFilter, int page, int size) {
        Pagination pagination = new Pagination(page, size);
        int tagsPassed = searchFilter.tags().size();
        Set<Tag> tags = searchFilter.tags().stream()
                .map(t -> tagRepository.findByName(t.getName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        int tagsFound = tags.size();
        if (tagsPassed != tagsFound) {
            return List.of();
        }

        searchFilter = searchFilter.updateTags(tags);
        return certificateRepository.findByFilterAndPage(searchFilter, pagination)
                .stream().map(certificateMapper::toCertificateDto).toList();
    }

    @Override
    @Transactional
    public CertificateDto updateNameById(Long id, CertificateDto certificateDto) {
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

        Certificate certificate = Certificate.builder()
                .name(certificateDto.getName())
                .description(certificateDto.getDescription())
                .price(certificateDto.getPrice())
                .duration(certificateDto.getDuration())
                .build();
        certificateRepository.save(certificate);
        tagRepository.setTags(certificate, tags);
        certificateDto = certificateMapper.toCertificateDto(certificate);
        certificateDto.setTags(tags.stream()
                .map(tagMapper::toTagDto).collect(Collectors.toSet()));
        return certificateDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        certificateRepository.delete(certificateRepository.findById(id));
    }
}
