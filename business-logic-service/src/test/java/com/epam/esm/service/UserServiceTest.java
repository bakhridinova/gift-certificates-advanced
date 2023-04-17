package com.epam.esm.service;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import com.epam.esm.util.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getUser;
import static com.epam.esm.util.TestDataFactory.getUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllByPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(userRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> userService.findById(anyLong()));
    }

    @Test
    void findAllByPageShouldReturnEmptyListIfNoUserWasFound() {
        when(userRepository.findAllByPage(any()))
                .thenReturn(List.of());

        assertTrue(userService
                .findAllByPage(0, 0).isEmpty());
    }

    @Test
    void findAllByPageShouldReturnCorrectListIfAnyUserWasFound() {
        when(userRepository.findAllByPage(any()))
                .thenReturn(List.of(getUser()));
        when(userMapper.toUserDto(any()))
                .thenReturn(getUserDto());

        assertEquals(List.of(getUserDto()),
                userService.findAllByPage(0, 0));
    }

    @Test
    void findByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(userRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> userService.findById(anyLong()));
    }

    @Test
    void findByIdShouldThrowCustomEntityNotFoundExceptionIfNoUserWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(userRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> userService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectUserIfUserWasFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(getUser());
        when(userMapper.toUserDto(any()))
                .thenReturn(getUserDto());

        assertEquals(getUserDto(),
                userService.findById(anyLong()));
    }
}