package com.epam.esm.dto.extra;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.entity.Tag;
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
 * @param pagination Pagination holding pagination details
 *
 * @author bakhridinova
 */
@Builder
public record SearchFilter(String name, String description,
                           String sortType, String sortOrder,
                           Set<TagDTO> tagDTOs, Set<Tag> tags,
                           Pagination pagination) {

    public SearchFilter inner(Set<Tag> tags) {
        return new SearchFilter(name(), description(), sortType(), sortOrder(), tagDTOs(), tags, pagination());
    }

    public int getSkip() {
        return pagination.getOffset();
    }

    public int getLimit() {
        return pagination.size();
    }

    public boolean isDescending() {
        return sortOrder.equals("desc");
    }
}
