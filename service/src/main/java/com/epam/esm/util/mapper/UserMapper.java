package com.epam.esm.util.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

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
     *
     * @param user User
     * @return UserDto
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    UserDto toUserDto(User user);
}
