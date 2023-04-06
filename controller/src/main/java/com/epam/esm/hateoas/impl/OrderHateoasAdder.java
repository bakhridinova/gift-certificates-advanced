package com.epam.esm.hateoas.impl;

import com.epam.esm.contoller.CertificateController;
import com.epam.esm.contoller.OrderController;
import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.OrderDTO;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderHateoasAdder implements HateoasAdder<OrderDTO> {

    @Override
    public void addLinksToEntity(OrderDTO order) {
        order.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                .getById(order.getId())).withSelfRel());
        order.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getById(order.getUserId())).withRel("user"));
        order.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CertificateController.class)
                .getById(order.getCertificateId())).withRel("certificate"));
    }
}
