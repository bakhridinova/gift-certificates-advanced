package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getUser;
import static com.epam.esm.util.TestDataFactory.getUserDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllShouldReturnEmptyListIfNoUserWasFound() {
        when(userRepository.findAll(any()))
                .thenReturn(List.of());

        assertTrue(userService
                .findAll(any()).isEmpty());
    }

    @Test
    void findAllShouldReturnCorrectListIfAnyUserWasFound() {
        when(userRepository.findAll(any()))
                .thenReturn(List.of(getUser()));
        when(userMapper.toUserDTO(any()))
                .thenReturn(getUserDTO());

        assertEquals(List.of(getUserDTO()),
                userService.findAll(any()));
    }

    @Test
    void findByIdShouldThrowExceptionIfNoUserWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(userRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> userService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectUserIfUserWasFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(getUser());
        when(userMapper.toUserDTO(any()))
                .thenReturn(getUserDTO());

        assertEquals(getUserDTO(),
                userService.findById(anyLong()));
    }
}