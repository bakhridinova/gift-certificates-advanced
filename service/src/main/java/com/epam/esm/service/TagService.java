package com.epam.esm.service;


import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.extra.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface TagService {
    List<TagDto> findAll(Pagination pagination);

    TagDto findById(Long id);
    TagDto findSpecial();

    TagDto create(TagDto tag);

    void delete(Long id);
}
