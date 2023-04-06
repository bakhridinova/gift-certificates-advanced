package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.mapper.CertificateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getCertificate;
import static com.epam.esm.util.TestDataFactory.getCertificateDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CertificateServiceImplTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CertificateMapper certificateMapper;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    void findAllShouldReturnEmptyListIfNoCertificateWasFound() {
        when(tagRepository.findAll(any()))
                .thenReturn(List.of());

        assertTrue(certificateService
                .findAll(any()).isEmpty());
    }

    @Test
    void findAllShouldReturnCorrectListIfAnyCertificateWasFound() {
        when(certificateRepository.findAll(any()))
                .thenReturn(List.of(getCertificate()));
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(List.of(getCertificateDto()),
                certificateService.findAll(any()));
    }

    @Test
    void findByIdShouldThrowExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectCertificateIfCertificateWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(getCertificateDto(),
                certificateService.findById(anyLong()));
    }

    @Test
    void updateNameShouldThrowExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.updateName(anyLong(), getCertificateDto()));
    }

    @Test
    void updateNameShouldReturnUpdatedCertificateIfCertificateWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(getCertificateDto(),
                certificateService.updateName(anyLong(), getCertificateDto()));
    }

    @Test
    void createShouldReturnCorrectCertificateIfCertificateWasCreated() {
        when(certificateMapper.toCertificate(any()))
                .thenReturn(getCertificate());
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());
        when(tagRepository.exists(anyString()))
                .thenReturn(false);

        assertEquals(getCertificateDto(), certificateService.create(getCertificateDto()));
    }

    @Test
    void deleteShouldThrowExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.delete(anyLong()));
    }

    @Test
    void deleteShouldDoNothingIfTagIfTagWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());

        assertDoesNotThrow(() -> certificateService.delete(anyLong()));
    }
}