package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getPagination;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserRepositoryTest extends RepositoryTest {
    private final UserRepository userRepository;
    @Spy
    private static JPAQueryFactory queryFactory;

    @Autowired
    public UserRepositoryTest(EntityManager entityManager,
                              UserRepository userRepository) {
        queryFactory = spy(new JPAQueryFactory(entityManager));
        this.userRepository = userRepository;
    }

    private static Pagination pagination;

    private static User user;

    @BeforeAll
    static void setUp() {
        pagination = getPagination(0, 10);
        queryFactory  = mock(JPAQueryFactory.class);
    }

    @BeforeEach
    void setUser() {
        user = new User();
        user.setUsername("");
        user.setPassword("");
        user.setFirstName("");
        user.setLastName("");
        user.setEmailAddress("");
        user.setOrders(List.of());
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(userRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(userRepository
                .findAll(pagination).isEmpty());
    }

    @Test
    @Order(3)
    public void findAllShouldReturnCorrectListAfterSaving() {
        userRepository.save(user);

        assertEquals(1, userRepository
                .findAll(pagination).size());

        assertEquals(user, userRepository
                .findAll(pagination).get(0));
    }

    @Test
    @Order(4)
    public void findAllShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(PersistenceException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> userRepository.findAll(pagination));
    }

    @Test
    @Order(6)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        userRepository.save(user);
        assertEquals(user, userRepository.findById(2L));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> userRepository.findById(1L));
    }
}