package com.epam.esm.service;


import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.extra.Pagination;

import java.util.List;

/**
 *
 * @author bakhridinova
 */

public interface OrderService {
    List<OrderDTO> findAll(Pagination pagination);

    OrderDTO findById(Long id);

    List<OrderDTO> findByUserId(Long userId, Pagination pagination);

    List<OrderDTO> findByCertificateId(Long certificateId, Pagination pagination);

    OrderDTO create(OrderDTO orderDTO);
}
