package com.epam.esm.service;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface CertificateService {
    List<CertificateDTO> findAll(Pagination pagination);

    CertificateDTO findById(Long id);

    List<CertificateDTO> findByFilter(SearchFilter searchFilter);

    CertificateDTO updateName(Long id, CertificateDTO certificate);

    CertificateDTO create(CertificateDTO certificate);

    void delete(Long id);
}
