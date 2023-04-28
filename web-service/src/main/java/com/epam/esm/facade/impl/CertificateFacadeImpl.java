package com.epam.esm.facade.impl;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.CertificateFacade;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.CertificateService;
import com.epam.esm.util.SearchFilter;
import com.epam.esm.util.enums.CertificateField;
import com.epam.esm.validator.CustomCertificateValidator;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomSortValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CertificateFacadeImpl implements CertificateFacade {
    private final CertificateService certificateService;
    private final HateoasAdder<CertificateDto> certificateHateoasAdder;
    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Override
    public List<CertificateDto> findAllByPage(int page, int size) {
        CustomPaginationValidator.validate(page, size);

        List<CertificateDto> certificates = certificateService.findAllByPage(page, size);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
    }

    @Override
    public CertificateDto findById(Long id) {
        CustomValidator.validateId(CertificateField.ID, id);

        CertificateDto certificate = certificateService.findById(id);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    @Override
    public List<CertificateDto> findByFilter(SearchFilter searchFilter, int page, int size) {
        CustomPaginationValidator.validate(page, size);
        CustomSortValidator.validate(searchFilter.sortType(), searchFilter.sortOrder());

        List<CertificateDto> certificates = certificateService.findByFilterAndPage(searchFilter, page, size);
        certificateHateoasAdder.addLinksToEntityList(certificates);
        return certificates;
    }

    @Override
    public CertificateDto create(CertificateDto certificateDto) {
        CustomCertificateValidator.validate(certificateDto);

        CertificateDto certificate = certificateService.create(certificateDto);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    @Override
    public CertificateDto updateNameById(Long id, CertificateDto certificateDto) {
        CustomValidator.validateId(CertificateField.ID, id);
        CustomCertificateValidator.validateName(certificateDto.getName());

        CertificateDto certificate = certificateService.updateNameById(id, certificateDto);
        certificateHateoasAdder.addLinksToEntity(certificate);
        return certificate;
    }

    @Override
    public CustomMessageHolder deleteById(Long id) {
        CustomValidator.validateId(CertificateField.ID, id);

        certificateService.deleteById(id);
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.OK, "certificate was successfully deleted");
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return messageHolder;
    }
}
