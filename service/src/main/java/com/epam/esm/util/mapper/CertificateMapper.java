package com.epam.esm.util.mapper;

import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * mapper to convert certificate into certificateDTO and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring",
        imports = {ArrayList.class, HashSet.class})
public interface CertificateMapper {
    /**
     * maps Certificate to CertificateDTO
     * sets number of orders of certificate to timesOrdered field
     *
     * @param certificate Certificate
     * @return CertificateDTO
     */
    @Mapping(target = "timesOrdered", expression = "java(certificate.getOrders() == null ? 0 : certificate.getOrders().size())")
    CertificateDTO toCertificateDTO(Certificate certificate);

    /**
     * maps CertificateDTO to Certificate
     * sets empty Set to tags field
     * sets empty List to orders field
     *
     * @param certificate CertificateDTO
     * @return Certificate
     */
    @Mapping(target = "tags", expression = "java(new HashSet())")
    @Mapping(target = "orders", expression = "java(new ArrayList())")
    Certificate toCertificate(CertificateDTO certificate);
}
