package com.epam.esm.util.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * mapper to convert Certificate into CertificateDto and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring",
        imports = {ArrayList.class, TreeSet.class})
public interface CertificateMapper {
    /**
     * maps Certificate to CertificateDto
     *
     * @param certificate Certificate
     * @return CertificateDto
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    CertificateDto toCertificateDto(Certificate certificate);
}
