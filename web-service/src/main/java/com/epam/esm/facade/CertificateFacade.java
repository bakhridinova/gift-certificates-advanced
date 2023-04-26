package com.epam.esm.facade;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.SearchFilter;

import java.util.List;

public interface CertificateFacade extends BaseFacade<CertificateDto> {
    @Override
    List<CertificateDto> findAllByPage(int page, int size);

    @Override
    CertificateDto findById(Long id);

    List<CertificateDto> findByFilter(SearchFilter filter, int page, int size);

    List<OrderDto> findByCertificateId(Long certificateId, int page, int size);

    CertificateDto updateNameById(Long id, CertificateDto certificateDto);
}
