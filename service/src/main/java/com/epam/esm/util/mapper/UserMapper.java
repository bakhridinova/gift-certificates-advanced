package com.epam.esm.util.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;

/**
 * mapper to convert User to UserDto and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring",
        imports = ArrayList.class)
public interface UserMapper {
    /**
     * maps User to UserDto
     * sets number of orders of user to timesOrdered field
     *
     * @param user User
     * @return UserDto
     */
    @Mapping(target = "timesOrdered", expression = "java(user.getOrders().size())")
    UserDto toUserDto(User user);
}
