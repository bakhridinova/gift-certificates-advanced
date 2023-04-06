package com.epam.esm.contoller;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import com.epam.esm.validator.CustomPaginationValidator;
import com.epam.esm.validator.CustomValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final HateoasAdder<UserDTO> userHateoasAdder;
    private final OrderService orderService;
    private final HateoasAdder<OrderDTO> orderHateoasAdder;

    /**
     * GET endpoint to retrieve list of users
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of UserDTO containing user details
     */
    @GetMapping
    public List<UserDTO> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size) {
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<UserDTO> users = userService.findAll(pagination);
        userHateoasAdder.addLinksToEntityList(users);
        return users;
    }

    /**
     * GET endpoint to retrieve specific user by its ID
     *
     * @param id Long ID of required user
     * @return CertificateDTO containing details of specified user
     */
    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {

        CustomValidator.validateId(id);
        return userService.findById(id);
    }

    /**
     * GET endpoint to search for retrieving orders associated with specific user
     *
     * @param id ID of user for which to retrieve orders
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of OrderDTO objects representing orders associated with user
     */
    @GetMapping("/{id}/orders")
    public List<OrderDTO> getOrders(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size) {
        CustomValidator.validateId(id);
        CustomPaginationValidator.validate(page, size);

        Pagination pagination = new Pagination(page, size);
        List<OrderDTO> orders = orderService.findByUserId(id, pagination);
        orderHateoasAdder.addLinksToEntityList(orders);
        return orders;
    }
}
