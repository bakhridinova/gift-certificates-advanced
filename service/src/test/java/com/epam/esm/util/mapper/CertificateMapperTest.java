package com.epam.esm.util.mapper;

import com.epam.esm.service.config.ServiceTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class CertificateMapperTest {
    private final CertificateMapper certificateMapper;

    @Autowired
    public CertificateMapperTest(CertificateMapper certificateMapper) {
        this.certificateMapper = certificateMapper;
    }

    @Test
    void shouldMapCertificatesCorrectlyTest() {
        assertEquals(getCertificate(),
                certificateMapper.toCertificate(getCertificateDTO()));
        assertEquals(getCertificateDTO(),
                certificateMapper.toCertificateDTO(getCertificate()));
    }

    @Test
    void shouldReturnNullIfNullPassedTest() {
        assertNull(certificateMapper.toCertificate(null));
        assertNull(certificateMapper.toCertificateDTO(null));
    }

    @Test
    void shouldReturnNullObjectIfNullObjectPassedTest() {
        assertEquals(getNullCertificate(),
                certificateMapper.toCertificate(getNullCertificateDTO()));
        assertEquals(getNullCertificateDTO(),
                certificateMapper.toCertificateDTO(getNullCertificate()));
    }
}