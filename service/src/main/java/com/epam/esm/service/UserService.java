package com.epam.esm.service;


import com.epam.esm.dto.UserDto;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * interface representing user related methods
 *
 * @author bakhridinova
 */

public interface UserService {
    /**
     * retrieves paginated list of all users
     *
     * @param pagination page and size
     * @return users that match pagination criteria
     */
    List<UserDto> findAll(Pagination pagination);

    /**
     * retrieves user  by its ID
     *
     * @param id ID of user to retrieve
     * @return user with given ID
     */
    UserDto findById(Long id);
}
