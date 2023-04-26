package com.epam.esm.facade.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.TagFacade;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.TagService;
import com.epam.esm.util.enums.TagField;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomTagValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TagFacadeImpl implements TagFacade {
    private final TagService tagService;
    private final HateoasAdder<TagDto> tagHateoasAdder;
    private final HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Override
    public List<TagDto> findAllByPage(int page, int size) {
        CustomPaginationValidator.validate(page, size);

        List<TagDto> tags = tagService.findAllByPage(page, size);
        tagHateoasAdder.addLinksToEntityList(tags);
        return tags;
    }

    @Override
    public TagDto findById(Long id) {
        CustomValidator.validateId(TagField.ID, id);

        TagDto tag = tagService.findById(id);
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }

    @Override
    public TagDto findSpecial() {
        TagDto tag = tagService.findSpecial();
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }

    @Override
    public TagDto create(TagDto tagDto) {
        CustomTagValidator.validate(tagDto);

        TagDto tag = tagService.create(tagDto);
        tagHateoasAdder.addLinksToEntity(tag);
        return tag;
    }

    @Override
    public CustomMessageHolder deleteById(Long id) {
        CustomValidator.validateId(TagField.ID, id);

        tagService.deleteById(id);
        CustomMessageHolder messageHolder = new CustomMessageHolder(
                HttpStatus.OK, "tag was successfully deleted");
        messageHolderHateoasAdder.addLinksToEntity(messageHolder);
        return messageHolder;
    }
}
