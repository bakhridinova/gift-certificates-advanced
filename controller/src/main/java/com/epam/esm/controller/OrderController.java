package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.Pagination;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final HateoasAdder<OrderDto> orderHateoasAdder;

    /**
     * GET endpoint to retrieve list of orders
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of orders
     */
    @GetMapping
    public List<OrderDto> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<OrderDto> orders = orderService.findAll(pagination);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }

    /**
     * GET endpoint to retrieve specific order by its ID
     *
     * @param id long ID of required order
     * @return specified order
     */
    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
        CustomValidator.validateId(id);

        OrderDto order = orderService.findById(id);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }


    /**
     * handles POST requests for creating new order
     *
     * @param orderDto representing new order to be created
     * @return order that was created
     */
    @PostMapping
    public OrderDto post(@RequestBody OrderDto orderDto) {
        CustomValidator.validateId(orderDto.getUserId());
        CustomValidator.validateId(orderDto.getCertificateId());

        OrderDto order = orderService.create(orderDto);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }
}
