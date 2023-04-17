package com.epam.esm.service;

import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.OrderServiceImpl;
import com.epam.esm.util.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getCertificate;
import static com.epam.esm.util.TestDataFactory.getOrder;
import static com.epam.esm.util.TestDataFactory.getOrderDto;
import static com.epam.esm.util.TestDataFactory.getPagination;
import static com.epam.esm.util.TestDataFactory.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {
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
    void findAllByPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(orderRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> orderService.findById(anyLong()));
    }

    @Test
    void findAllByPageShouldReturnEmptyListIfNoOrderWasFound() {
        when(orderRepository.findAllByPage(any()))
                .thenReturn(List.of());

        assertTrue(orderService
                .findAllByPage(0, 0).isEmpty());
    }

    @Test
    void findAllByPageShouldReturnCorrectListIfAnyOrderWasFound() {

        when(orderRepository.findAllByPage(any()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findAllByPage(0, 0));
    }

    @Test
    void findByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(orderRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> orderService.findById(anyLong()));
    }

    @Test
    void findByIdShouldThrowCustomEntityNotFoundExceptionIfNoOrderWasFound() {
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
    void findByUserIdAndPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(orderRepository).findByUserId(any(), any());

        assertThrows(DataAccessException.class,
                () -> orderService.findByUserIdAndPage(0L, 0, 0));
    }

    @Test
    void findByUserIdAndPageShouldReturnCorrectListIfUserWasFound() {
        when(userRepository.findById(anyLong()))
                .thenReturn(getUser());
        when(orderRepository.findByUserId(0L, getPagination()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findByUserIdAndPage(0L, 0, 0));
    }

    @Test
    void findByCertificateIdAndPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(orderRepository).findByCertificateId(any(), any());

        assertThrows(DataAccessException.class,
                () -> orderService.findByCertificateIdAndPage(0L, 0, 0));
    }

    @Test
    void findByCertificateIdAndPageShouldReturnCorrectListIfCertificateWasFound() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        when(orderRepository.findByCertificateId(0L, getPagination()))
                .thenReturn(List.of(getOrder()));
        when(orderMapper.toOrderDto(any()))
                .thenReturn(getOrderDto());

        assertEquals(List.of(getOrderDto()),
                orderService.findByCertificateIdAndPage(0L, 0, 0));
    }

    @Test
    void createShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        when(certificateRepository.findById(anyLong()))
                .thenReturn(getCertificate());
        doThrow(new DataAccessException("") {})
                .when(orderRepository).save(any());

        assertThrows(DataAccessException.class,
                () -> orderService.create(getOrderDto()));
    }

    @Test
    void createShouldThrowCustomEntityNotFoundExceptionIfNoUserWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(userRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> orderService.create(getOrderDto()));
    }

    @Test
    void createShouldThrowCustomEntityNotFoundExceptionIfNoCertificateWasFound() {
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