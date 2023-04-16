package com.epam.esm.controller;

import com.epam.esm.GiftCertificatesAdvancedApplication;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getOrderDto;
import static com.epam.esm.util.TestDataFactory.getUserDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = GiftCertificatesAdvancedApplication.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    // beans controllers are dependent on
    @MockBean
    private HateoasAdder<UserDto> userHateoasAdder;
    @MockBean
    private HateoasAdder<OrderDto> orderHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAllShouldReturnEmptyListIfUsersWereNotFound() throws Exception {
        when(userService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/users")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAllShouldReturnCorrectListIfUsersWereFound() throws Exception {
        when(userService.findAll(any()))
                .thenReturn(List.of(getUserDto()));
        this.mockMvc.perform(get("/api/users"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$..username", String.class)
                        .value(""))
                .andExpect(jsonPath("$..password", String.class)
                        .value(""))
                .andExpect(jsonPath("$..firstName", String.class)
                        .value(""))
                .andExpect(jsonPath("$..lastName", String.class)
                        .value(""));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size must be between 0 and 100"));
    }

    @Test
    void getByIdShouldReturnCorrectUserIfUserWasFound() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(getUserDto());
        this.mockMvc.perform(get("/api/users/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.username", String.class)
                        .value(""))
                .andExpect(jsonPath("$.password", String.class)
                        .value(""))
                .andExpect(jsonPath("$.firstName", String.class)
                        .value(""))
                .andExpect(jsonPath("$.lastName", String.class)
                        .value(""));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfUserWasNotFound() throws Exception {
        when(userService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find user by id 1"));
        this.mockMvc.perform(get("/api/users/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find user by id 1"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void getOrdersShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/users/1/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfUserWasNotFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenThrow(new CustomEntityNotFoundException("failed to find user by id 1"));
        this.mockMvc.perform(get("/api/users/1/orders"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find user by id 1"));
    }

    @Test
    void getOrdersShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/users/1/orders"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$..price", Double.class)
                        .value(0.0))
                .andExpect(jsonPath("$..userId", Long.class)
                        .value(0))
                .andExpect(jsonPath("$..certificateId", Long.class)
                        .value(0));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users/text/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users/-1/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }
}