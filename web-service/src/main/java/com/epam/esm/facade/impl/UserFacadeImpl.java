package com.epam.esm.facade.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.util.enums.OrderField;
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
    private final OrderService orderService;
    private final HateoasAdder<OrderDto> orderHateoasAdder;

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
        return userService.findById(id);
    }

    @Override
    public List<OrderDto> findByUserId(Long id, int page, int size) {
        CustomValidator.validateId(OrderField.ID, id);
        CustomPaginationValidator.validate(page, size);

        List<OrderDto> orders = orderService.findByUserIdAndPage(id, page, size);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }
}
