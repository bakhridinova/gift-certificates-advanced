package com.epam.esm.facade;

import com.epam.esm.dto.OrderDto;

import java.util.List;

public interface OrderFacade extends BaseFacade<OrderDto> {
    @Override
    List<OrderDto> findAllByPage(int page, int size);

    @Override
    OrderDto findById(Long id);

    List<OrderDto> findByCertificateOrUserId(Long certificateId, Long userId, int page, int size);
}
