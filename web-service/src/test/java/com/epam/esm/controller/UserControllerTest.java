package com.epam.esm.controller;

import com.epam.esm.GiftCertificatesAdvancedApplication;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.UserFacade;
import com.epam.esm.facade.impl.UserFacadeImpl;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getUserDto;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = { UserFacadeImpl.class,
        GiftCertificatesAdvancedApplication.class })
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserFacade userFacade;

    @MockBean
    private UserService userService;
    @MockBean
    private HateoasAdder<UserDto> userHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAllByPageShouldReturnEmptyListIfUsersWereNotFound() throws Exception {
        when(userService.findAllByPage(anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/users")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAllByPageShouldReturnCorrectListIfUsersWereFound() throws Exception {
        when(userService.findAllByPage(anyInt(), anyInt()))
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
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page number should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page size should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page number must be between 0 and 10000"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/users").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page size must be between 0 and 100"));
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
                        .value("user id must be positive"));
    }
}