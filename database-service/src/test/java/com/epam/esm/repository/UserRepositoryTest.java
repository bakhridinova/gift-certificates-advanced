package com.epam.esm.repository;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.atomic.AtomicInteger;

import static com.epam.esm.util.TestDataFactory.getPagination;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRepositoryTest extends RepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private static Pagination pagination;

    @BeforeAll
    static void setUp() {
        pagination = getPagination(0, 10);
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(userRepository);
    }

    @Test
    @Order(2)
    public void findAllByPageShouldReturnInitialList() {
        assertEquals(10, userRepository
                .findAllByPage(pagination).size());

        AtomicInteger counter = new AtomicInteger(1);
        userRepository.findAllByPage(pagination)
                .forEach((user) -> {
                    int value = counter.get();
                    assertEquals(value,
                            user.getId());
                    assertEquals("test" + value,
                            user.getUsername());
                    assertEquals("test" + value,
                            user.getPassword());
                    assertEquals("test" + value,
                            user.getFirstName());
                    assertEquals("test" + value,
                            user.getLastName());
                    assertEquals("test" + value,
                            user.getEmailAddress());
                    counter.incrementAndGet();
                });
    }

    @Test
    @Order(3)
    public void findTotalNumberShouldReturnInitialLong() {
        assertEquals(10, userRepository.findTotalNumber());
    }

    @Test
    @Order(4)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        User user = userRepository.findById(1L);
        assertEquals(1,
                user.getId());
        assertEquals("test" + 1,
                user.getUsername());
        assertEquals("test" + 1,
                user.getPassword());
        assertEquals("test" + 1,
                user.getFirstName());
        assertEquals("test" + 1,
                user.getLastName());
        assertEquals("test" + 1,
                user.getEmailAddress());
    }

    @Test
    @Order(5)
    public void findByIdShouldThrowCustomEntityNotFoundExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> userRepository.findById(Long.MAX_VALUE));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowDataAccessExceptionIfCertificateIdIsNull() {
        assertThrows(DataAccessException.class,
                () -> userRepository.findById(null));
    }
}