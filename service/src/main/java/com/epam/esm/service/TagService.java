package com.epam.esm.service;


import com.epam.esm.dto.TagDto;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * interface representing tag related methods
 *
 * @author bakhridinova
 */

public interface TagService {
    /**
     * retrieves paginated list of all tags
     *
     * @param pagination page and size
     * @return tags that match pagination criteria
     */
    List<TagDto> findAll(Pagination pagination);

    /**
     * retrieves tag by its ID
     *
     * @param id of tag to retrieve
     * @return tag with given ID
     */
    TagDto findById(Long id);

    /**
     * retrieves the most commonly used tag of a user
     * with the highest cost of all orders
     *
     * @return specified tag
     */
    TagDto findSpecial();

    /**
     * creates new tag
     *
     * @param tag to create
     * @return created tag with its ID and other fields populated
     */
    TagDto create(TagDto tag);

    /**
     * deletes tag with given ID
     *
     * @param id of tag to delete
     */
    void delete(Long id);
}
