package com.epam.esm.util.mapper;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
import org.mapstruct.Mapper;

/**
 * mapper to convert tag into tagDTO and vice versa
 *
 * @author bakhridinova
 * */

@Mapper(componentModel = "spring")
public interface TagMapper {
    /**
     * maps Tag to TagDTO
     *
     * @param tag Tag
     * @return TagDTO
     */
    TagDTO toTagDTO(Tag tag);

    /**
     * maps TagDTO to Tag
     *
     * @param tag TagDTO
     * @return Tag
     */
    Tag toTag(TagDTO tag);
}
