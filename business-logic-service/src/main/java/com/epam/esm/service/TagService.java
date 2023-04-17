package com.epam.esm.service;


import com.epam.esm.dto.TagDto;

import java.util.List;

/**
 * interface holding business logic for tags
 *
 * @author bakhridinova
 */

public interface TagService {
    List<TagDto> findAllByPage(int page, int size);

    TagDto findById(Long id);
    TagDto findSpecial();

    TagDto create(TagDto tag);

    void deleteById(Long id);
}
