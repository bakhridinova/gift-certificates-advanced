package com.epam.esm.repository;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderRepositoryTest extends RepositoryTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    private static Pagination pagination;
    @BeforeAll
    static void setUp() {
        pagination = new Pagination(0, 10);
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(orderRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnInitialList() {
        assertEquals(10, orderRepository
                .findAll(pagination).size());

        AtomicInteger counter = new AtomicInteger(1);
        orderRepository.findAll(pagination)
                .forEach((order) -> {
                    int value = counter.get();
                    assertEquals(value,
                            order.getId());
                    assertEquals(value,
                            order.getPrice());
                    assertEquals(value,
                            order.getUser().getId());
                    assertEquals(value,
                            order.getCertificate().getId());
                    counter.incrementAndGet();
                });
    }

    @Test
    @Order(3)
    public void findTotalNumberShouldReturnInitialLong() {
        assertEquals(10, orderRepository.findTotalNumber());
    }

    @Test
    @Order(4)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        com.epam.esm.entity.Order order = orderRepository.findById(1L);
        assertEquals(1,
                order.getId());
        assertEquals(1,
                order.getPrice());
        assertEquals(1,
                order.getUser().getId());
        assertEquals(1,
                order.getCertificate().getId());
    }

    @Test
    @Order(5)
    public void findByIdShouldThrowCustomEntityNotFoundExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> orderRepository.findById(Long.MAX_VALUE));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowDataAccessExceptionIfCertificateIdIsNull() {
        assertThrows(DataAccessException.class,
                () -> orderRepository.findById(null));
    }

    @Test
    @Order(7)
    public void findByUserShouldReturnCorrectListIfUserExists() {
        User user = userRepository.findById(1L);
        List<com.epam.esm.entity.Order> orders =
                orderRepository.findByUser(user, pagination);
        assertEquals(1, orders.size());
        com.epam.esm.entity.Order order = orders.get(0);
        assertEquals(1,
                order.getId());
        assertEquals(1,
                order.getPrice());
        assertEquals(1,
                order.getUser().getId());
        assertEquals(1,
                order.getCertificate().getId());
    }

    @Test
    @Order(8)
    public void findByUserShouldThrowDataAccessExceptionIfUserDoesNotExist() {
        assertThrows(DataAccessException.class,
                () -> orderRepository.findByUser(User.builder().build(), pagination));
    }

    @Test
    @Order(9)
    public void findByCertificateShouldReturnCorrectCertificateIfCertificateExists() {
        Certificate certificate = certificateRepository.findById(1L);
        List<com.epam.esm.entity.Order> orders =
                orderRepository.findByCertificate(certificate, pagination);
        assertEquals(1, orders.size());
        com.epam.esm.entity.Order order = orders.get(0);
        assertEquals(1,
                order.getId());
        assertEquals(1,
                order.getPrice());
        assertEquals(1,
                order.getUser().getId());
        assertEquals(1,
                order.getCertificate().getId());
    }

    @Test
    @Order(12)
    public void findByCertificateShouldThrowDataAccessExceptionIfCertificateIdIsNull() {
        assertThrows(DataAccessException.class,
                () -> orderRepository.findByCertificate(new Certificate(), pagination));
    }

    @Test
    @Order(13)
    public void saveShouldAddNewRecordToDataBase() {
        com.epam.esm.entity.Order order = new com.epam.esm.entity.Order();
        order.setPrice(0.0);
        order.setUser(userRepository.findById(10L));
        order.setCertificate(certificateRepository.findById(10L));
        orderRepository.save(order);
        assertEquals(order,
                orderRepository.findById(11L));
    }

    @Test
    @Order(14)
    public void saveShouldThrowDataAccessExceptionIfPriceIsNull() {
        com.epam.esm.entity.Order order = new com.epam.esm.entity.Order();
        order.setPrice(null);
        assertThrows(DataAccessException.class,
                () -> orderRepository.save(order));
    }
}