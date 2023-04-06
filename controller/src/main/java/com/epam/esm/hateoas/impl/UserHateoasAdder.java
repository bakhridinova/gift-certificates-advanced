package com.epam.esm.hateoas.impl;

import com.epam.esm.contoller.UserController;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserHateoasAdder implements HateoasAdder<UserDTO> {

    @Override
    public void addLinksToEntity(UserDTO user) {
        user.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getById(user.getId())).withSelfRel());
        user.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getOrders(user.getId(), 0, 5)).withRel("orders"));
    }
}