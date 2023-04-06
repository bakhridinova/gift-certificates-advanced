package com.epam.esm.service;


import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.extra.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface UserService {
    List<UserDto> findAll(Pagination pagination);

    UserDto findById(Long id);
}
