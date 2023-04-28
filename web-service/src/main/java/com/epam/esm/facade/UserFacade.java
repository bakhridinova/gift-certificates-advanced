package com.epam.esm.facade;

import com.epam.esm.dto.UserDto;

import java.util.List;

public interface UserFacade extends BaseFacade<UserDto> {
    @Override
    List<UserDto> findAllByPage(int page, int size);

    @Override
    UserDto findById(Long id);
}
