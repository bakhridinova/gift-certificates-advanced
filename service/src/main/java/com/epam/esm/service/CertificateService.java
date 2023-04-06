package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface CertificateService {
    List<CertificateDto> findAll(Pagination pagination);

    CertificateDto findById(Long id);

    List<CertificateDto> findByFilter(SearchFilter searchFilter);

    CertificateDto updateName(Long id, CertificateDto certificate);

    CertificateDto create(CertificateDto certificate);

    void delete(Long id);
}
