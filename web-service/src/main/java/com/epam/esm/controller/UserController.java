package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.facade.UserFacade;
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
    private final UserFacade userFacade;

    /**
     * GET endpoint to retrieve list of users
     *
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of users
     */
    @GetMapping
    public List<UserDto> getAllByPage(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size) {
        return userFacade.findAllByPage(page, size);
    }

    /**
     * GET endpoint to retrieve specific user by its ID
     *
     * @param id long ID of required user
     * @return specified user
     */
    @GetMapping("/{id}")
    public UserDto getById(@PathVariable long id) {
        return userFacade.findById(id);
    }

    /**
     * GET endpoint to search for retrieving orders associated with specific user
     *
     * @param id long ID of user for which to retrieve orders
     * @param page page number requested (default is 0)
     * @param size number of items per page (default is 5)
     * @return List of orders associated with user
     */
    @GetMapping("/{id}/orders")
    public List<OrderDto> getOrdersByPage(@PathVariable long id,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "5") int size) {
        return userFacade.findByUserId(id, page, size);
    }
}
