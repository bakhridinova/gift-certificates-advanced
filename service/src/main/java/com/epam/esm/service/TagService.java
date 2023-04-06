package com.epam.esm.service;


import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface TagService {
    List<TagDTO> findAll(Pagination pagination);

    TagDTO findById(Long id);
    TagDTO findSpecial();

    TagDTO create(TagDTO tag);

    void delete(Long id);
}
