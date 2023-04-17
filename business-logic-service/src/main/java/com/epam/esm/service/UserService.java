package com.epam.esm.service;


import com.epam.esm.dto.UserDto;

import java.util.List;

/**
 * interface holding business logic for users
 *
 * @author bakhridinova
 */

public interface UserService {
    List<UserDto> findAllByPage(int page, int size);

    UserDto findById(Long id);
}
