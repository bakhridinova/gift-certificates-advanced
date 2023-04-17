package com.epam.esm.util;

import com.epam.esm.dto.TagDto;
import lombok.Builder;

import java.util.Set;

/**
 * record representing search filter
 *
 * @param name String text from name to use for search results
 * @param description String text from description to use for search results
 * @param sortType String type of sorting to use for search results
 * @param sortOrder String order of sorting to use for the search results
 * @param tags set of tags to filter search results by, can be empty
 *
 * @author bakhridinova
 */
@Builder
public record SearchFilter(String name, String description,
                           String sortType, String sortOrder,
                           Set<TagDto> tags) {

    public String sortOrder() {
        return sortOrder == null ? "asc" : sortOrder;
    }

    public String sortType() {
        return sortType == null ? "id" : sortType;
    }

    public boolean isDescending() {
        return sortOrder.equals("desc");
    }

    public Set<TagDto> tags() {
        return tags == null ? Set.of() : tags;
    }
}
