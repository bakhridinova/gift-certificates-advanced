package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.repository.OrderRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.epam.esm.util.TestDataFactory.getPagination;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderRepositoryTest extends RepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void contextLoads() {
        assertNotNull(orderRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(orderRepository.findAll(getPagination(0, 10)).isEmpty());
    }
}