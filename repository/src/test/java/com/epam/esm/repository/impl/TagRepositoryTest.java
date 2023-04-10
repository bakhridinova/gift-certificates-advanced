package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.TagRepository;
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

public class TagRepositoryTest extends RepositoryTest {
    private final TagRepository tagRepository;
    @Spy
    private final JPAQueryFactory queryFactory;

    @Autowired
    public TagRepositoryTest(EntityManager entityManager,
                             TagRepository tagRepository) {
        queryFactory = spy(new JPAQueryFactory(entityManager));
        this.tagRepository = tagRepository;
    }

    private Pagination pagination;
    private Tag tag;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setName("");

        pagination = new Pagination(0, 10);
    }


    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(tagRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(tagRepository
                .findAll(pagination).isEmpty());
    }

    @Test
    @Order(3)
    public void findAllShouldReturnCorrectListAfterSaving() {
        tagRepository.save(tag);

        assertEquals(1, tagRepository
                .findAll(pagination).size());

        assertEquals(tag, tagRepository
                .findAll(pagination).get(0));
    }

    @Test
    @Order(4)
    public void findAllShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(PersistenceException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> tagRepository.findAll(pagination));
    }

    @Test
    @Order(6)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        tagRepository.save(tag);
        assertEquals(tag, tagRepository.findById(2L));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> tagRepository.findById(1L));
    }
}