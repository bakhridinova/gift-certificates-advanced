package com.epam.esm.service;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.CertificateServiceImpl;
import com.epam.esm.util.mapper.CertificateMapper;
import com.epam.esm.util.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getCertificate;
import static com.epam.esm.util.TestDataFactory.getCertificateDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class CertificateServiceTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private CertificateMapper certificateMapper;
    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @Test
    void findAllByPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(certificateRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> certificateService.findById(anyLong()));
    }

    @Test
    void findAllByPageShouldReturnEmptyListIfNoCertificateWasFound() {
        when(tagRepository.findAllByPage(any()))
                .thenReturn(List.of());

        assertTrue(certificateService
                .findAllByPage(0, 0).isEmpty());
    }

    @Test
    void findAllByPageShouldReturnCorrectListIfAnyCertificateWasFound() {
        when(certificateRepository.findAllByPage(any()))
                .thenReturn(List.of(getCertificate()));
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(List.of(getCertificateDto()),
                certificateService.findAllByPage(0, 0));
    }

    @Test
    void findByIdShouldThrowCustomEntityNotFoundExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.findById(anyLong()));
    }

    @Test
    void findByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(certificateRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
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
    void updateNameByIdShouldThrowCustomEntityNotFoundExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.updateNameById(anyLong(), getCertificateDto()));
    }

    @Test
    void updateNameByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(certificateRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> certificateService.updateNameById(anyLong(), getCertificateDto()));
    }

    @Test
    void updateNameByIdShouldReturnUpdatedCertificateIfCertificateWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(getCertificateDto(),
                certificateService.updateNameById(anyLong(), getCertificateDto()));
    }

    @Test
    void createShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(certificateRepository).save(any());

        assertThrows(DataAccessException.class,
                () -> certificateService.create(getCertificateDto()));
    }

    @Test
    void createShouldReturnCorrectCertificateIfCertificateWasCreated() {
        when(certificateMapper.toCertificateDto(any()))
                .thenReturn(getCertificateDto());

        assertEquals(getCertificateDto(), certificateService.create(getCertificateDto()));
    }

    @Test
    void deleteByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(certificateRepository).delete(any());

        assertThrows(DataAccessException.class,
                () -> certificateService.deleteById(anyLong()));
    }

    @Test
    void deleteByIdShouldThrowCustomEntityNotFoundExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateService.deleteById(anyLong()));
    }

    @Test
    void deleteByIdShouldDoNothingIfTagIfTagWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());

        assertDoesNotThrow(() -> certificateService.deleteById(anyLong()));
    }
}