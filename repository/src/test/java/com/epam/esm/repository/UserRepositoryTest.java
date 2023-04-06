package com.epam.esm.repository;

import com.epam.esm.container.CustomPostgreSQLContainer;
import com.epam.esm.repository.impl.UserRepositoryImpl;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserRepositoryImpl.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @ClassRule
    public static PostgreSQLContainer<CustomPostgreSQLContainer> postgreSQLContainer = CustomPostgreSQLContainer.getInstance();

    @Test
    public void test() {
        assertNotNull(userRepository);
    }
}