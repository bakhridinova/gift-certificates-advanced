package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class CertificateHateoasAdder implements HateoasAdder<CertificateDto> {

    @Override
    public void addLinksToEntity(CertificateDto certificate) {
        certificate.add(WebMvcLinkBuilder.linkTo(CertificateController.class)
                .withRel("GET all certificates"));
        certificate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getById(certificate.getId())).withRel("GET certificate " + certificate.getId()));
        certificate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getOrdersByPage(certificate.getId(), 0, 5)).withRel("GET certificate's orders"));

        if (certificate.getUserId() != null) {
            certificate.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getById(certificate.getUserId())).withRel("GET certificate's user"));
        }

        certificate.getTags().forEach((tag ->
                tag.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                        TagController.class).getById(tag.getId())).withRel("GET tag " + tag.getId()))));
    }
}
