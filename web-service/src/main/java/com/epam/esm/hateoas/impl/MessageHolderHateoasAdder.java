package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.TagController;
import com.epam.esm.controller.UserController;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class MessageHolderHateoasAdder implements HateoasAdder<CustomMessageHolder> {

    @Override
    public void addLinksToEntity(CustomMessageHolder message) {
        message.add(WebMvcLinkBuilder.linkTo(CertificateController.class)
                .withRel("certificates ->"));
        message.add(WebMvcLinkBuilder.linkTo(TagController.class)
                .withRel("tags ->"));
        message.add(WebMvcLinkBuilder.linkTo(OrderController.class)
                .withRel("orders ->"));
        message.add(WebMvcLinkBuilder.linkTo(UserController.class)
                .withRel("users ->"));
    }
}
