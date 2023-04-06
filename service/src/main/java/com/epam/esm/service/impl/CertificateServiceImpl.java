package com.epam.esm.service.impl;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;
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
    public List<CertificateDTO> findAll(Pagination pagination) {
        return certificateRepository.findAll(pagination).stream()
                .map(certificateMapper::toCertificateDTO).toList();
    }

    @Override
    public CertificateDTO findById(Long id) {
        return certificateMapper.toCertificateDTO(certificateRepository.findById(id));
    }

    @Override
    public List<CertificateDTO> findByFilter(SearchFilter searchFilter) {
        Set<Tag> tags = searchFilter.tagDTOs().stream()
                .filter(t -> tagRepository.exists(t.getName()))
                .map(tagMapper::toTag)
                .collect(Collectors.toSet());

        searchFilter = searchFilter.inner(tags);

        return certificateRepository.findByFilter(searchFilter)
                .stream().map(certificateMapper::toCertificateDTO).toList();
    }

    @Override
    public CertificateDTO updateName(Long id, CertificateDTO certificateDTO) {
        Certificate certificate = certificateRepository.findById(id);
        certificate.setDescription(certificate.getDescription());
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateDTO(certificate);
    }

    @Override
    @Transactional
    public CertificateDTO create(CertificateDTO certificateDTO) {
        for (TagDTO tagDTO : certificateDTO.getTags()) {
            String name = tagDTO.getName();
            if (!tagRepository.exists(name)) {
                Tag tag = new Tag();
                tag.setName(name);
                tagRepository.save(tag);
            }
        }

        Certificate certificate = certificateMapper.toCertificate(certificateDTO);
        certificateRepository.save(certificate);
        return certificateMapper.toCertificateDTO(certificate);
    }

    @Override
    public void delete(Long id) {
        certificateRepository.delete(certificateRepository.findById(id));
    }
}
