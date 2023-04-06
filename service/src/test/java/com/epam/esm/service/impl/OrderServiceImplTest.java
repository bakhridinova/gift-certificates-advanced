package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findAllShouldReturnEmptyListIfNoOrderWasFound() {
        when(orderRepository.findAll(any()))
                .thenReturn(List.of());

        assertTrue(orderService
                .findAll(any()).isEmpty());
    }

    @Test
    void findAllShouldReturnCorrectListIfAnyOrderWasFound() {

        when(orderRepository.findAll(any()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findAll(any()));
    }

    @Test
    void findByIdShouldThrowExceptionIfNoOrderWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(orderRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectOrderIfOrderWasFound() {
        when(orderRepository.findById(anyLong()))
                .thenReturn(getOrder());
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(getOrderDto(),
                orderService.findById(anyLong()));
    }

    @Test
    void findByUserShouldThrowExceptionIfNoUserWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(userRepository).findById(anyLong());
        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.findByUserId(anyLong(), getPagination()));
    }

    @Test
    void findByUserShouldReturnCorrectListIfUserWasFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(getUser());
        when(orderRepository.findByUser(getUser(), getPagination()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findByUserId(anyLong(), getPagination()));
    }

    @Test
    void findByCertificateShouldThrowExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());
        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.findByCertificateId(anyLong(), getPagination()));
    }

    @Test
    void findByCertificateShouldReturnCorrectListIfCertificateWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(orderRepository.findByCertificate(getCertificate(), getPagination()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findByCertificateId(anyLong(), getPagination()));
    }

    @Test
    void createShouldThrowExceptionIfNoUserWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(userRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.create(getOrderDto()));
    }

    @Test
    void createShouldThrowExceptionIfNoCertificateWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(certificateRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.create(getOrderDto()));
    }

    @Test
    void createShouldReturnCorrectOrderIfUserAndCertificateWereFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(userRepository.findById(anyLong()))
                .thenReturn(getUser());
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(getOrderDto(), orderService.create(getOrderDto()));
    }
}