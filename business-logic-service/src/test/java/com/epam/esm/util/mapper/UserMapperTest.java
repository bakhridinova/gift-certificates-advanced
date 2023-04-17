package com.epam.esm.util.mapper;

import com.epam.esm.config.ServiceTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.util.TestDataFactory.getNullUser;
import static com.epam.esm.util.TestDataFactory.getNullUserDto;
import static com.epam.esm.util.TestDataFactory.getUser;
import static com.epam.esm.util.TestDataFactory.getUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class UserMapperTest {
    private final UserMapper userMapper;

    @Autowired
    public UserMapperTest(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    void shouldMapUsersCorrectlyTest() {
        assertEquals(getUserDto(),
                userMapper.toUserDto(getUser()));
    }

    @Test
    void shouldReturnNullIfNullPassedTest() {
        assertNull(userMapper.toUserDto(null));
    }

    @Test
    void shouldReturnNullObjectIfNullObjectPassedTest() {
        assertEquals(getNullUserDto(),
                userMapper.toUserDto(getNullUser()));
    }
}