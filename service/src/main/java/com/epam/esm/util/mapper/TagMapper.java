package com.epam.esm.util.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;

/**
 * mapper to convert Tag into TagDto and vice versa
 *
 * @author bakhridinova
 * */

@Mapper(componentModel = "spring")
public interface TagMapper {
    /**
     * maps Tag to TagDto
     *
     * @param tag Tag
     * @return TagDto
     */
    TagDto toTagDto(Tag tag);

    /**
     * maps TagDto to Tag
     *
     * @param tag TagDto
     * @return Tag
     */
    Tag toTag(TagDto tag);
}
