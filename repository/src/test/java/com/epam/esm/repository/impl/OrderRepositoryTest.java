package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

public class OrderRepositoryTest extends RepositoryTest {
    private final OrderRepository orderRepository;
    @Spy
    private final JPAQueryFactory queryFactory;

    @Autowired
    public OrderRepositoryTest(EntityManager entityManager,
                               OrderRepository orderRepository) {
        queryFactory = spy(new JPAQueryFactory(entityManager));
        this.orderRepository = orderRepository;
    }

    private com.epam.esm.entity.Order order;
    private Pagination pagination;

    @BeforeEach
    void setUp() {
        order = new com.epam.esm.entity.Order();
        order.setPrice(0.0);

        pagination = new Pagination(0, 10);
    }


    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(orderRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(orderRepository
                .findAll(pagination).isEmpty());
    }

    @Test
    @Order(3)
    public void findAllShouldReturnCorrectListAfterSaving() {
        orderRepository.save(order);

        assertEquals(1, orderRepository
                .findAll(pagination).size());

        assertEquals(order, orderRepository
                .findAll(pagination).get(0));
    }

    @Test
    @Order(4)
    public void findAllShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(PersistenceException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> orderRepository.findAll(pagination));
    }

    @Test
    @Order(6)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        orderRepository.save(order);
        assertEquals(order, orderRepository.findById(2L));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> orderRepository.findById(1L));
    }
}