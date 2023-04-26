package com.epam.esm.facade;

import com.epam.esm.dto.TagDto;

import java.util.List;

public interface TagFacade extends BaseFacade<TagDto> {
    @Override
    List<TagDto> findAllByPage(int page, int size);

    @Override
    TagDto findById(Long id);

    TagDto findSpecial();
}
