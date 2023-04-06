package com.epam.esm.repository;

import com.epam.esm.container.CustomPostgreSQLContainer;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TagRepositoryImpl.class)
public class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepository;

    @ClassRule
    public static PostgreSQLContainer<CustomPostgreSQLContainer> postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @Test
    public void test() {
        assertNotNull(tagRepository);
    }
}