package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAllByPage(int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return orderRepository.findAllByPage(pagination)
                .stream().map(orderMapper::toOrderDto).toList();
    }

    @Override
    public OrderDto findById(Long id) {
        return orderMapper.toOrderDto(orderRepository.findById(id));
    }

    @Override
    public List<OrderDto> findByUserIdAndPage(Long userId, int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return orderRepository.findByUserId(userId, pagination)
                .stream().map(orderMapper::toOrderDto).toList();
    }

    @Override
    public List<OrderDto> findByCertificateIdAndPage(Long certificateId, int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return orderRepository.findByCertificateId(certificateId, pagination)
                .stream().map(orderMapper::toOrderDto).toList();
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto orderDto) {
        User user = userRepository
                .findById(orderDto.getUserId());
        Certificate certificate = certificateRepository
                .findById(orderDto.getCertificateId());

        Order order = Order.builder()
                .price(certificate.getPrice())
                .user(user).certificate(certificate)
                .build();
        orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }
}
