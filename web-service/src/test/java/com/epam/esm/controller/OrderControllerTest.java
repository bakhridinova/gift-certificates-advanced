package com.epam.esm.controller;

import com.epam.esm.GiftCertificatesAdvancedApplication;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.facade.OrderFacade;
import com.epam.esm.facade.impl.OrderFacadeImpl;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getOrderDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ContextConfiguration(classes = { OrderFacadeImpl.class,
        GiftCertificatesAdvancedApplication.class })
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderFacade orderFacade;
    @MockBean
    private OrderService orderService;

    @MockBean
    private HateoasAdder<OrderDto> orderHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAllByPageShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findAllByPage(anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAllByPageShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findAllByPage(anyInt(), anyInt()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/orders"))
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
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page number should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page size should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page number must be between 0 and 10000"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page size must be between 0 and 100"));
    }

    @Test
    void getByIdShouldReturnCorrectOrderIfOrderWasFound() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(getOrderDto());
        this.mockMvc.perform(get("/api/orders/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.price", Double.class)
                        .value(0.0))
                .andExpect(jsonPath("$.userId", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.certificateId", Long.class)
                        .value(0));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfOrderWasNotFound() throws Exception {
        when(orderService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find order by id 1"));
        this.mockMvc.perform(get("/api/orders/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find order by id 1"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("order id must be positive"));
    }

    @Test
    void createShouldReturnCorrectOrderIfOrderWasCreated() throws Exception {
        when(orderService.create(any()))
                .thenReturn(getOrderDto());
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", 1)
                                .put("certificateId", 1)
                                .toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.price", Double.class)
                        .value(0.0))
                .andExpect(jsonPath("$.userId", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.certificateId", Long.class)
                        .value(0));
    }

    @Test
    void createShouldThrowExceptionWithCorrectMessageIfUserIdIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", null)
                                .put("certificateId", 1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("user id should not be null"));
    }

    @Test
    void createShouldThrowExceptionWithCorrectMessageIfCertificateIdIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", 1)
                                .put("certificateId", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate id should not be null"));
    }

    @Test
    void createShouldThrowExceptionWithCorrectMessageIfUserIdIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", -1)
                                .put("certificateId", 1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("user id must be positive"));
    }

    @Test
    void createShouldThrowExceptionWithCorrectMessageIfCertificateIdIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", 1)
                                .put("certificateId", -1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate id must be positive"));
    }

    @Test
    void getByCertificateIdShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/orders/search")
                .param("certificateId", "1")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getByCertificateIdShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/orders/search")
                        .param("certificateId", "1"))
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
    void getByCertificateIdShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/orders/search")
                        .param("certificateId", "1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getByCertificateIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders/search")
                        .param("certificateId", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificateId should be of type java.lang.Long"));
    }

    @Test
    void getByCertificateIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders/search")
                        .param("certificateId", "-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate id must be positive"));
    }

    @Test
    void getByUserIdShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findByUserIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/orders/search")
                .param("userId", "1")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getByUserIdShouldThrowExceptionWithCorrectMessageIfUserWasNotFound() throws Exception {
        when(orderService.findByUserIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenThrow(new CustomEntityNotFoundException("failed to find user by id 1"));
        this.mockMvc.perform(get("/api/orders/search")
                        .param("userId", "1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find user by id 1"));
    }

    @Test
    void getByUserIdShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findByUserIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/orders/search")
                        .param("userId", "1"))
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
    void getByUserIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders/search")
                        .param("userId", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("userId should be of type java.lang.Long"));
    }

    @Test
    void getByUserIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders/search")
                        .param("userId", "-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("user id must be positive"));
    }

    @Test
    void getByCertificateOrUserIdShouldThrowExceptionWithCorrectMessageIfNeitherOfParametersPassed() throws Exception {
        this.mockMvc.perform(get("/api/orders/search"))
        .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("either certificateId or userId should be passed, not both or neither"));
    }

    @Test
    void getByCertificateOrUserIdShouldThrowExceptionWithCorrectMessageIfBothParametersPassed() throws Exception {
        this.mockMvc.perform(get("/api/orders/search")
                        .param("userId", "1")
                        .param("certificateId", "1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("either certificateId or userId should be passed, not both or neither"));
    }
}