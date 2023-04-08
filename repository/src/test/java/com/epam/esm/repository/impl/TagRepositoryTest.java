package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.epam.esm.util.TestDataFactory.getPagination;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TagRepositoryTest extends RepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @Test
    public void contextLoads() {
        assertNotNull(tagRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(tagRepository.findAll(getPagination(0, 10)).isEmpty());
    }
}