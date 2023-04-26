package com.epam.esm.service;


import com.epam.esm.dto.OrderDto;

import java.util.List;

/**
 * interface holding business logic for orders
 *
 * @author bakhridinova
 */

public interface OrderService {
    List<OrderDto> findAllByPage(int page, int size);

    OrderDto findById(Long id);

    List<OrderDto> findByUserIdAndPage(Long userId, int page, int size);

    List<OrderDto> findByCertificateIdAndPage(Long certificateId, int page, int size);

    OrderDto create(OrderDto order);
}
