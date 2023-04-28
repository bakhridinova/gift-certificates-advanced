package com.epam.esm.facade.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.UserService;
import com.epam.esm.util.enums.UserField;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
    private final UserService userService;
    private final HateoasAdder<UserDto> userHateoasAdder;

    @Override
    public List<UserDto> findAllByPage(int page, int size) {
        CustomPaginationValidator.validate(page, size);

        List<UserDto> users = userService.findAllByPage(page, size);
        userHateoasAdder.addLinksToEntityList(users);
        return users;
    }

    @Override
    public UserDto findById(Long id) {
        CustomValidator.validateId(UserField.ID, id);

        UserDto user = userService.findById(id);
        userHateoasAdder.addLinksToEntity(user);
        return user;
    }
}
