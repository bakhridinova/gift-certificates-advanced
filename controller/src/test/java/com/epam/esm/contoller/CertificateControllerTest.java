package com.epam.esm.contoller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.epam.esm.util.TestDataFactory.getCertificateDto;
import static com.epam.esm.util.TestDataFactory.getOrderDto;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CertificateController.class)
class CertificateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @MockBean
    private CertificateService certificateService;

    // beans controllers are dependent on
    @MockBean
    private HateoasAdder<OrderDto> orderHateoasAdder;
    @MockBean
    private HateoasAdder<CertificateDto> certificateHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAll_ShouldReturn_EmptyList_IfCertificatesWereNotFound() throws Exception {
        when(certificateService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAll_ShouldReturn_CorrectList_IfCertificatesWereFound() throws Exception {
        when(certificateService.findAll(any()))
                .thenReturn(List.of(getCertificateDto()));
        this.mockMvc.perform(get("/api/certificates"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$..name", String.class)
                        .value(""))
                .andExpect(jsonPath("$..description", String.class)
                        .value(""))
                .andExpect(jsonPath("$..price", String.class)
                        .value(0.0))
                .andExpect(jsonPath("$..duration", String.class)
                        .value(0))
                .andExpect(jsonPath("$..tags.length()", is(0)));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size must be between 0 and 100"));
    }

    @Test
    void getById_ShouldReturn_CorrectCertificate_IfCertificateWasFound() throws Exception {
        when(certificateService.findById(anyLong()))
                .thenReturn(getCertificateDto());
        this.mockMvc.perform(get("/api/certificates/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.name", String.class)
                        .value(""))
                .andExpect(jsonPath("$.description", String.class)
                        .value(""))
                .andExpect(jsonPath("$.price", String.class)
                        .value(0.0))
                .andExpect(jsonPath("$.duration", String.class)
                        .value(0))
                .andExpect(jsonPath("$..tags.length()", is(0)));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfCertificateWasNotFound() throws Exception {
        when(certificateService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/certificates/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void search_ShouldReturn_EmptyList_IfCertificatesWereNotFound() throws Exception {
        when(certificateService.findByFilter(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/search")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void search_ShouldReturn_CorrectList_IfCertificatesWereFound() throws Exception {
        when(certificateService.findByFilter(any()))
                .thenReturn(List.of(getCertificateDto()));
        this.mockMvc.perform(get("/api/certificates/search"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$..name", String.class)
                        .value(""))
                .andExpect(jsonPath("$..description", String.class)
                        .value(""))
                .andExpect(jsonPath("$..price", String.class)
                        .value(0.0))
                .andExpect(jsonPath("$..duration", String.class)
                        .value(0));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size must be between 0 and 100"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortTypeIsEmpty() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortType", ""))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType should not be empty"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortOrderIsEmpty() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortOrder", ""))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder should not be empty"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortTypeIsBlank() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortType", " "))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType should not be blank"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortOrderIsBlank() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortOrder", " "))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder should not be blank"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortTypeIsInvalid() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortType", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType must be either name, price, duration, createdAt or lastUpdatedAt"));
    }

    @Test
    void search_ShouldThrowException_WithCorrectMessage_IfSortOrderIsInvalid() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortOrder", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder must be either asc or desc"));
    }

    @Test
    void getOrders_ShouldReturn_EmptyList_IfOrdersWereNotFound() throws Exception {
        when(orderService.findByCertificateId(anyLong(), any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/1/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getOrders_ShouldReturn_CorrectList_IfOrdersWereFound() throws Exception {
        when(orderService.findByCertificateId(anyLong(), any()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..price", Double.class).value(0.0))
                .andExpect(jsonPath("$..userId", Long.class).value(0))
                .andExpect(jsonPath("$..certificateId", Long.class).value(0));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfCertificateWasNotFound() throws Exception {
        when(orderService.findByCertificateId(anyLong(), any()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/text/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getOrders_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/-1/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void post_ShouldReturn_CorrectCertificate_IfCertificateWasCreated() throws Exception {
        when(certificateService.create(any()))
                .thenReturn(getCertificateDto());
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..name", String.class)
                        .value(""))
                .andExpect(jsonPath("$..description", String.class)
                        .value(""))
                .andExpect(jsonPath("$..price", String.class)
                        .value(0.0))
                .andExpect(jsonPath("$..duration", String.class)
                        .value(0))
                .andExpect(jsonPath("$..tags.length()", is(0)));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", null)
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", null)
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfPriceIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", null)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("price should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDurationIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", null)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("duration should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfTagsIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("tags should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIsEmpty() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be empty"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIsEmpty() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description should not be empty"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", " ")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be blank"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", " ")
                                .put("price", 1.00)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description should not be blank"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "*")
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "*")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", new JSONArray())
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must be between 3 and 60 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(31, "*")))
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description",
                                        String.join("", Collections.nCopies(61, "*")))
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must be between 3 and 60 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfCertificateNameIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(3, "*")))
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must include only letters and spaces"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDescriptionIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description",
                                        String.join("", Collections.nCopies(3, "*")))
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must include only letters and spaces"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfPriceIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", -1.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("price should not be negative"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDurationIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", -1)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("duration should not be negative"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfPriceIsTooLow() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", 1.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("price must be between 10 and 100"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDurationIsNTooLow() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 1)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("duration must be between 10 and 90"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfPriceIsTooHigh() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description", "test")
                                .put("price", 101.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("price must be between 10 and 100"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfDurationIsTooHigh() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 101)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("duration must be between 10 and 90"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIsNull() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name", null)))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be null"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIsEmpty() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name", "")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be empty"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIsBlank() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name", " ")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be blank"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIsTooShort() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name",
                                                String.join("", Collections.nCopies(2, "*")))))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 20 characters"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIsTooLong() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name",
                                                String.join("", Collections.nCopies(21, "*")))))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 20 characters"));
    }

    @Test
    void post_shouldThrowException_WithCorrectMessage_IfTagNameIncludesSpecial() throws Exception {
        JSONObject certificateJsonObject = new JSONObject();
        JSONObject tagJsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(certificateJsonObject
                                .put("name", "test")
                                .put("description","test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray.put(
                                        tagJsonObject.put("name",
                                                String.join("", Collections.nCopies(3, "*")))))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must include only letters"));
    }

    @Test
    void patch_ShouldReturn_CorrectCertificate_IfCertificateWasFound() throws Exception {
        JSONObject jsonObject = new JSONObject();
        when(certificateService.updateName(anyLong(), any()))
                .thenReturn(getCertificateDto());
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class)
                        .value(0))
                .andExpect(jsonPath("$.name", String.class)
                        .value(""))
                .andExpect(jsonPath("$.description", String.class)
                        .value(""))
                .andExpect(jsonPath("$.price", String.class)
                        .value(0.0))
                .andExpect(jsonPath("$.duration", String.class)
                        .value(0))
                .andExpect(jsonPath("$..tags.length()", is(0)));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfCertificateWasNotFound() throws Exception {
        JSONObject jsonObject = new JSONObject();
        when(certificateService.updateName(anyLong(), any()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                        .put("name", "test")
                        .toString()))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(patch("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be null"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIsEmpty() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be empty"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", " ")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be blank"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "*")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(31, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void patch_ShouldThrowException_WithCorrectMessage_IfNameIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(3, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must include only letters and spaces"));
    }

    @Test
    void delete_ShouldReturn_CorrectMessage_IfCertificateWasFound() throws Exception {
        doNothing().when(certificateService).delete(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate was successfully deleted"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfCertificateWasNotFound() throws Exception {
        doThrow(new CustomEntityNotFoundException("failed to find tag by id 1"))
                .when(certificateService).delete(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find tag by id 1"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }
}