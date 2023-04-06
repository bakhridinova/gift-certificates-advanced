package com.epam.esm.service;


import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.extra.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface UserService {
    List<UserDTO> findAll(Pagination pagination);

    UserDTO findById(Long id);
}
