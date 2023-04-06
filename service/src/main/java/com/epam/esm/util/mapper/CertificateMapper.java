package com.epam.esm.util.mapper;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * mapper to convert Certificate into CertificateDto and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring",
        imports = {ArrayList.class, HashSet.class})
public interface CertificateMapper {
    /**
     * maps Certificate to CertificateDto
     * sets number of orders of certificate to timesOrdered field
     *
     * @param certificate Certificate
     * @return CertificateDto
     */
    @Mapping(target = "timesOrdered", expression = "java(certificate.getOrders() == null ? 0 : certificate.getOrders().size())")
    CertificateDto toCertificateDto(Certificate certificate);

    /**
     * maps CertificateDto to Certificate
     * sets empty Set to tags field
     * sets empty List to orders field
     *
     * @param certificate CertificateDto
     * @return Certificate
     */
    @Mapping(target = "tags", expression = "java(new HashSet())")
    @Mapping(target = "orders", expression = "java(new ArrayList())")
    Certificate toCertificate(CertificateDto certificate);
}
