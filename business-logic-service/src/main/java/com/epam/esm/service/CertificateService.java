package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.util.SearchFilter;

import java.util.List;

/**
 * interface holding business logic for certificates
 *
 * @author bakhridinova
 */

public interface CertificateService {
    List<CertificateDto> findAllByPage(int page, int size);

    CertificateDto findById(Long id);

    List<CertificateDto> findByFilterAndPage(SearchFilter searchFilter, int page, int size);

    CertificateDto updateNameById(Long id, CertificateDto certificate);

    CertificateDto create(CertificateDto certificate);

    void deleteById(Long id);
}
