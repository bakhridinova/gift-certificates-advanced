package com.epam.esm.util.mapper;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;

/**
 * mapper to convert user to userDTO and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring",
        imports = ArrayList.class)
public interface UserMapper {
    /**
     * maps User to UserDTO
     * sets number of orders of user to timesOrdered field
     *
     * @param user User
     * @return UserDTO
     */
    @Mapping(target = "timesOrdered", expression = "java(user.getOrders().size())")
    UserDTO toUserDTO(User user);
}
