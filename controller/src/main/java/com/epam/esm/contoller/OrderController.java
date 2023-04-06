package com.epam.esm.contoller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.extra.Pagination;
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
    private final HateoasAdder<OrderDTO> orderHateoasAdder;

    /**
     * GET endpoint to retrieve list of orders
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of OrderDTO containing order details
     */
    @GetMapping
    public List<OrderDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<OrderDTO> orders = orderService.findAll(pagination);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }

    /**
     * GET endpoint to retrieve specific order by its ID
     *
     * @param id Long ID of required order
     * @return CertificateDTO containing details of specified order
     */
    @GetMapping("/{id}")
    public OrderDTO getById(@PathVariable Long id) {
        CustomValidator.validateId(id);

        OrderDTO order = orderService.findById(id);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }


    /**
     * handles POST requests for creating new order
     *
     * @param orderDTO OrderDTO object representing new order to be created
     * @return OrderDTO object that was created
     */
    @PostMapping
    public OrderDTO post(@RequestBody OrderDTO orderDTO) {
        CustomValidator.validateId(orderDTO.getUserId());
        CustomValidator.validateId(orderDTO.getCertificateId());

        OrderDTO order = orderService.create(orderDTO);
        orderHateoasAdder.addLinksToEntity(order);
        return order;
    }
}
