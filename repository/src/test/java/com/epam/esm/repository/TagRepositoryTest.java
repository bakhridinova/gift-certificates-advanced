package com.epam.esm.repository;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.util.Pagination;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class TagRepositoryTest extends RepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    private static Pagination pagination;

    @BeforeAll
    static void setUp() {
        pagination = new Pagination(0, 10);
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(tagRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnInitialList() {
        assertEquals(9, tagRepository
                .findAll(pagination).size());

        AtomicInteger counter = new AtomicInteger(1);
        tagRepository.findAll(pagination)
                .forEach((tag) -> {
                    int value = counter.get();
                    assertEquals(value,
                            tag.getId());
                    assertEquals("test" + value,
                            tag.getName());
                    counter.incrementAndGet();
                });
    }

    @Test
    @Order(4)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        Tag tag = tagRepository.findById(1L);
        assertEquals(1,
                tag.getId());
        assertEquals("test1",
                tag.getName());
    }

    @Test
    @Order(5)
    public void findByIdShouldThrowCustomEntityNotFoundExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> tagRepository.findById(Long.MAX_VALUE));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowDataAccessExceptionIfCertificateIdIsNull() {
        assertThrows(DataAccessException.class,
                () -> tagRepository.findById(null));
    }

    @Test
    @Order(7)
    public void existsShouldReturnTrueIfTagsWithSuchNameExists() {
        IntStream.range(1, tagRepository.findAll(pagination).size())
                .forEach((i) -> assertTrue(tagRepository.exists("test" + i)));
    }

    @Test
    @Order(8)
    public void existsShouldReturnFalseIfTagsWithSuchNameDoesNotExist() {
        IntStream.range(1, tagRepository.findAll(pagination).size())
                .forEach((i) -> assertFalse(tagRepository.exists("test name" + i)));
    }

    @Test
    @Order(9)
    public void saveShouldAddNewRecordToDataBase() {
        Tag tag = new Tag();
        tag.setName("test10");
        tagRepository.save(tag);
        assertEquals(tag, tagRepository.findById(10L));
    }

    @Test
    @Order(10)
    public void saveShouldThrowDataAccessExceptionIfNameIsNull() {
        Tag tag = new Tag();
        tag.setName(null);
        assertThrows(DataAccessException.class,
                () -> tagRepository.save(tag));
    }

    @Test
    @Order(11)
    public void deleteShouldRemoveRecordFromDatabase() {
        Tag tag = new Tag();
        tag.setName("test12");
        tagRepository.save(tag);
        assertEquals(10,
                tagRepository.findAll(pagination).size());
        tagRepository.delete(tag);
        assertEquals(9,
                tagRepository.findAll(pagination).size());
    }

    @Test
    @Order(12)
    public void deleteShouldThrowDataAccessExceptionIfEntityIsAlreadyDetached() {
        Tag tag = new Tag();
        tag.setName("test13");
        tagRepository.save(tag);
        assertEquals(10,
                tagRepository.findAll(pagination).size());
        tagRepository.delete(tag);
        assertEquals(9,
                tagRepository.findAll(pagination).size());
        assertThrows(DataAccessException.class,
                () -> tagRepository.delete(tag));
    }
}