package com.epam.esm.hateoas.impl;

import com.epam.esm.contoller.CertificateController;
import com.epam.esm.contoller.TagController;
import com.epam.esm.dto.CertificateDTO;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CertificateHateoasAdder implements HateoasAdder<CertificateDTO> {

    @Override
    public void addLinksToEntity(CertificateDTO certificate) {
        certificate.add(WebMvcLinkBuilder.linkTo(CertificateController.class)
                .withRel("GET all certificates"));
        certificate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getById(certificate.getId())).withRel("GET certificate " + certificate.getId()));
        certificate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getOrders(certificate.getId(), 0, 5)).withRel("GET certificate's orders"));

        certificate.getTags().forEach((tag ->
                tag.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                        TagController.class).getById(tag.getId())).withRel("GET tag " + tag.getId()))));
    }
}
