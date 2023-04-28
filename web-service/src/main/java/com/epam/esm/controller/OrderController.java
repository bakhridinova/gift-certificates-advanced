package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.facade.OrderFacade;
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
    private final OrderFacade orderFacade;

    /**
     * GET endpoint to retrieve list of orders
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of orders
     */
    @GetMapping
    public List<OrderDto> getAllByPage(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
       return orderFacade.findAllByPage(page, size);
    }

    /**
     * GET endpoint to retrieve specific order by its ID
     *
     * @param id long ID of required order
     * @return specified order
     */
    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable long id) {
       return orderFacade.findById(id);
    }


    /**
     * handles POST requests for creating new order
     *
     * @param orderDto representing new order to be created
     * @return order that was created
     */
    @PostMapping
    public OrderDto create(@RequestBody OrderDto orderDto) {
        return orderFacade.create(orderDto);
    }

    /**
     * GET endpoint to search for retrieving orders associated with specific certificate or user
     *
     * @param certificateId ID of certificate for which to retrieve orders
     * @param userId ID of user for which to retrieve orders
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of orders associated with certificate
     */
    @GetMapping("/search")
    public List<OrderDto> getByCertificateOrUserId(@RequestParam(required = false) Long certificateId,
                                                   @RequestParam(required = false) Long userId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size) {
        return orderFacade.findByCertificateOrUserId(certificateId, userId, page, size);
    }
}
