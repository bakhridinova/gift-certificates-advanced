package com.epam.esm.service.impl;

import com.epam.esm.dto.OrderDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CertificateRepository certificateRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDTO> findAll(Pagination pagination) {
        return orderRepository.findAll(pagination)
                .stream().map(orderMapper::toOrderDTO).toList();
    }

    @Override
    public OrderDTO findById(Long id) {
        return orderMapper.toOrderDTO(orderRepository.findById(id));
    }

    @Override
    public List<OrderDTO> findByUserId(Long userId, Pagination pagination) {
        User user = userRepository.findById(userId);
        return orderRepository.findByUser(user, pagination)
                .stream().map(orderMapper::toOrderDTO).toList();
    }

    @Override
    public List<OrderDTO> findByCertificateId(Long certificateId, Pagination pagination) {
        Certificate certificate = certificateRepository.findById(certificateId);
        return orderRepository.findByCertificate(certificate, pagination)
                .stream().map(orderMapper::toOrderDTO).toList();
    }

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        User user = userRepository
                .findById(orderDTO.getUserId());
        Certificate certificate = certificateRepository
                .findById(orderDTO.getCertificateId());

        Order order = new Order();
        order.setUser(user);
        order.setPrice(certificate.getPrice());
        order.setCertificate(certificate);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
        return orderMapper.toOrderDTO(order);
    }
}
