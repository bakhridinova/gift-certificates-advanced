package com.epam.esm.util.mapper;

import com.epam.esm.config.ServiceTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.util.TestDataFactory.getCertificate;
import static com.epam.esm.util.TestDataFactory.getCertificateDto;
import static com.epam.esm.util.TestDataFactory.getNullCertificate;
import static com.epam.esm.util.TestDataFactory.getNullCertificateDto;
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
        assertEquals(getCertificateDto(),
                certificateMapper.toCertificateDto(getCertificate()));
    }

    @Test
    void shouldReturnNullIfNullPassedTest() {
        assertNull(certificateMapper.toCertificateDto(null));
    }

    @Test
    void shouldReturnNullObjectIfNullObjectPassedTest() {
        assertEquals(getNullCertificateDto(),
                certificateMapper.toCertificateDto(getNullCertificate()));
    }
}