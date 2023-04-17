package com.epam.esm.util.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;

/**
 * mapper to convert Tag into TagDto and vice versa
 *
 * @author bakhridinova
 * */

@Mapper(componentModel = "spring",
        imports = HashSet.class)
public interface TagMapper {
    /**
     * maps Tag to TagDto
     *
     * @param tag Tag
     * @return TagDto
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    TagDto toTagDto(Tag tag);

    /**
     * maps TagDto to Tag
     *
     * @param tag TagDto
     * @return Tag
     */
    @Mapping(target = "certificates", expression = "java(new HashSet<>())")
    Tag toTag(TagDto tag);
}
