package com.epam.esm.contoller;

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
    void getAll_ShouldReturn_EmptyList_IfUsersWereNotFound() throws Exception {
        when(userService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/users")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAll_ShouldReturn_CorrectList_IfUsersWereFound() throws Exception {
        when(userService.findAll(any()))
                .thenReturn(List.of(getUserDto()));
        this.mockMvc.perform(get("/api/users"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..username", String.class).value(""))
                .andExpect(jsonPath("$..password", String.class).value(""))
                .andExpect(jsonPath("$..firstName", String.class).value(""))
                .andExpect(jsonPath("$..lastName", String.class).value(""))
                .andExpect(jsonPath("$..timesOrdered", Integer.class).value(0));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page must be between 0 and 10000"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size must be between 0 and 100"));
    }

    @Test
    void getById_ShouldReturn_CorrectUser_IfUserWasFound() throws Exception {
        when(userService.findById(anyLong()))
                .thenReturn(getUserDto());
        this.mockMvc.perform(get("/api/users/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.username", String.class).value(""))
                .andExpect(jsonPath("$.password", String.class).value(""))
                .andExpect(jsonPath("$.firstName", String.class).value(""))
                .andExpect(jsonPath("$.lastName", String.class).value(""))
                .andExpect(jsonPath("$.timesOrdered", Integer.class).value(0));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfUserWasNotFound() throws Exception {
        when(userService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find user by id 1"));
        this.mockMvc.perform(get("/api/users/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class).value("failed to find user by id 1"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should be of type long"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }

    @Test
    void getOrders_ShouldReturn_EmptyList_IfOrdersWereNotFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/users/1/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfUserWasNotFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenThrow(new CustomEntityNotFoundException("failed to find user by id 1"));
        this.mockMvc.perform(get("/api/users/1/orders"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class).value("failed to find user by id 1"));
    }

    @Test
    void getOrders_ShouldReturn_CorrectList_IfOrdersWereFound() throws Exception {
        when(orderService.findByUserId(anyLong(), any()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/users/1/orders"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..price", Double.class).value(0.0))
                .andExpect(jsonPath("$..userId", Long.class).value(0))
                .andExpect(jsonPath("$..certificateId", Long.class).value(0));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users/text/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should be of type long"));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users/-1/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }
}