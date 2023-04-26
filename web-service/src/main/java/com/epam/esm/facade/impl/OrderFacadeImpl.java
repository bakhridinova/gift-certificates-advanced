package com.epam.esm.facade.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.enums.CertificateField;
import com.epam.esm.util.enums.OrderField;
import com.epam.esm.util.enums.UserField;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
    private final OrderService orderService;
    private final HateoasAdder<OrderDto> orderHateoasAdder;

    @Override
    public List<OrderDto> findAllByPage(int page, int size) {
        CustomPaginationValidator.validate(page, size);

        List<OrderDto> orders = orderService.findAllByPage(page, size);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }

    @Override
    public OrderDto findById(Long id) {
        CustomValidator.validateId(OrderField.ID, id);

        OrderDto order = orderService.findById(id);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }

    @Override
    public OrderDto create(OrderDto orderDto) {
        CustomValidator.validateId(UserField.ID, orderDto.getUserId());
        CustomValidator.validateId(CertificateField.ID, orderDto.getCertificateId());

        OrderDto order = orderService.create(orderDto);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }
}
