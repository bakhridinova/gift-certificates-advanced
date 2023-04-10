package com.epam.esm.service;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 * interface representing order related methods
 *
 * @author bakhridinova
 */

public interface OrderService {
    /**
     * retrieves paginated list of all orders
     *
     * @param pagination page and size
     * @return orders that match pagination criteria
     */
    List<OrderDto> findAll(Pagination pagination);

    /**
     * retrieves order  by its ID
     *
     * @param id ID of order to retrieve
     * @return order with given ID
     */
    OrderDto findById(Long id);

    /**
     * retrieves paginated list of orders containing specified user ID
     *
     * @param userId ID of user to search for
     * @param pagination page and size
     *
     * @return list of orders containing specified user ID
     */
    List<OrderDto> findByUser(Long userId, Pagination pagination);

    /**
     * retrieves paginated list of orders containing specified certificate ID
     *
     * @param certificateId ID of certificate to search for
     * @param pagination page and size
     *
     * @return list of orders containing specified certificate ID
     */
    List<OrderDto> findByCertificate(Long certificateId, Pagination pagination);

    /**
     * creates new order
     *
     * @param order to create
     * @return created order with its ID and other fields populated
     */
    OrderDto create(OrderDto order);
}
