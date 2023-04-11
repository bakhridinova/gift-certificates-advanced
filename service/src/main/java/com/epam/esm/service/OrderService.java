package com.epam.esm.service;


import com.epam.esm.dto.OrderDto;
import com.epam.esm.util.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface OrderService {
    List<OrderDto> findAll(Pagination pagination);

    OrderDto findById(Long id);

    List<OrderDto> findByUserId(Long userId, Pagination pagination);

    List<OrderDto> findByCertificateId(Long certificateId, Pagination pagination);

    OrderDto create(OrderDto order);
}
