package com.epam.esm.contoller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.OrderService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getOrderDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    // beans controllers are dependent on
    @MockBean
    private HateoasAdder<OrderDto> orderHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAll_ShouldReturn_EmptyList_IfOrdersWereNotFound() throws Exception {
        when(orderService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAll_ShouldReturn_CorrectList_IfOrdersWereFound() throws Exception {
        when(orderService.findAll(any()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/orders"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..price", Double.class).value(0.0))
                .andExpect(jsonPath("$..userId", Long.class).value(0))
                .andExpect(jsonPath("$..certificateId", Long.class).value(0));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page must be between 0 and 10000"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/orders").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size must be between 0 and 100"));
    }

    @Test
    void getById_ShouldReturn_CorrectOrder_IfOrderWasFound() throws Exception {
        when(orderService.findById(anyLong()))
                .thenReturn(getOrderDto());
        this.mockMvc.perform(get("/api/orders/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.price", Double.class).value(0.0))
                .andExpect(jsonPath("$.userId", Long.class).value(0))
                .andExpect(jsonPath("$.certificateId", Long.class).value(0));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfOrderWasNotFound() throws Exception {
        when(orderService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find order by id 1"));
        this.mockMvc.perform(get("/api/orders/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class).value("failed to find order by id 1"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/orders/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should be of type long"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/orders/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }

    @Test
    void post_ShouldReturn_CorrectOrder_IfOrderWasCreated() throws Exception {
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
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.price", Double.class).value(0.0))
                .andExpect(jsonPath("$.userId", Long.class).value(0))
                .andExpect(jsonPath("$.certificateId", Long.class).value(0));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfUserIdIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", null)
                                .put("certificateId", 1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateIdIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", 1)
                                .put("certificateId", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfUserIdIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", -1)
                                .put("certificateId", 1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateIdIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("userId", 1)
                                .put("certificateId", -1)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }


}