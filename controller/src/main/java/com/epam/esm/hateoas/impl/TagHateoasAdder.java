package com.epam.esm.hateoas.impl;

import com.epam.esm.contoller.TagController;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.hateoas.HateoasAdder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TagHateoasAdder implements HateoasAdder<TagDTO> {

    @Override
    public void addLinksToEntity(TagDTO tag) {
        tag.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                .getById(tag.getId())).withSelfRel());
    }
}
