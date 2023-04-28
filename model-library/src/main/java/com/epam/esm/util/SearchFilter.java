package com.epam.esm.util;

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
 *
 * @author bakhridinova
 */
@Builder
public record SearchFilter(String name, String description,
                           String sortType, String sortOrder,
                           Set<Tag> tags) {
    public String name() {
        return name == null ? "" : name;
    }

    public String description() {
        return description == null ? "" : description;
    }

    public String sortOrder() {
        return sortOrder == null ? "asc" : sortOrder;
    }

    public String sortType() {
        return sortType == null ? "id" : sortType;
    }

    public boolean isDescending() {
        return sortOrder().equals("desc");
    }

    public Set<Tag> tags() {
        return tags == null ? Set.of() : tags;
    }

    public SearchFilter updateTags(Set<Tag> tags) {
        return new SearchFilter(name(), description(), sortType(), sortOrder(), tags);
    }
}
