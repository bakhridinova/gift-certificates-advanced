package com.epam.esm.controller;

import com.epam.esm.GiftCertificatesAdvancedApplication;
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
import org.springframework.test.context.ContextConfiguration;
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
@ContextConfiguration(classes = GiftCertificatesAdvancedApplication.class)
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
    void getAllShouldReturnEmptyListIfCertificatesWereNotFound() throws Exception {
        when(certificateService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAllShouldReturnCorrectListIfCertificatesWereFound() throws Exception {
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
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void getAllShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size must be between 0 and 100"));
    }

    @Test
    void getByIdShouldReturnCorrectCertificateIfCertificateWasFound() throws Exception {
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
    void getByIdShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        when(certificateService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/certificates/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getByIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void searchShouldReturnEmptyListOfCertificatesWereNotFound() throws Exception {
        when(certificateService.findByFilter(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/search")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void searchShouldReturnCorrectListIfCertificatesWereFound() throws Exception {
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
    void searchShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size must be between 0 and 100"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortTypeIsBlank() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortType", " "))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType should not be empty or blank"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortOrderIsBlank() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortOrder", " "))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder should not be empty or blank"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortTypeIsInvalid() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortType", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType must be either name, price, duration, createdAt or lastUpdatedAt"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortOrderIsInvalid() throws Exception {
        this.mockMvc.perform(get("/api/certificates/search")
                        .param("sortOrder", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder must be either asc or desc"));
    }

    @Test
    void getOrdersShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findByCertificate(anyLong(), any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/1/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getOrdersShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findByCertificate(anyLong(), any()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..price", Double.class).value(0.0))
                .andExpect(jsonPath("$..userId", Long.class).value(0))
                .andExpect(jsonPath("$..certificateId", Long.class).value(0));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        when(orderService.findByCertificate(anyLong(), any()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/text/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getOrdersShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/-1/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void postShouldReturnCorrectCertificateIfCertificateWasCreated() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfCertificateNameIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfDescriptionIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfPriceIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfDurationIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfTagsIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfCertificateNameIsBlank() throws Exception {
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
                        .value("name should not be empty or blank"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfDescriptionIsBlank() throws Exception {
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
                        .value("description should not be empty or blank"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfCertificateNameIsTooShort() throws Exception {
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
                        .value("name must be between 4 and 40 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfDescriptionIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description",
                                        String.join("", Collections.nCopies(3, "*")))
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", new JSONArray())
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must be between 4 and 100 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfCertificateNameIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(41, "*")))
                                .put("description", "test")
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 4 and 40 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfDescriptionIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description",
                                        String.join("", Collections.nCopies(101, "*")))
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must be between 4 and 100 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfCertificateNameIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(4, "*")))
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
    void postShouldThrowExceptionWithCorrectMessageIfDescriptionIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        this.mockMvc.perform(post("/api/certificates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .put("description",
                                        String.join("", Collections.nCopies(4, "*")))
                                .put("price", 10.0)
                                .put("duration", 10)
                                .put("tags", jsonArray)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("description must include only letters and spaces"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfPriceIsNegative() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfDurationIsNegative() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfPriceIsTooLow() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfDurationIsNTooLow() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfPriceIsTooHigh() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfDurationIsTooHigh() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfTagNameIsNull() throws Exception {
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
    void postShouldThrowExceptionWithCorrectMessageIfTagNameIsBlank() throws Exception {
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
                        .value("name should not be empty or blank"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfTagNameIsTooShort() throws Exception {
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
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfTagNameIsTooLong() throws Exception {
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
                                                String.join("", Collections.nCopies(31, "*")))))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 30 characters"));
    }

    @Test
    void postShouldThrowExceptionWithCorrectMessageIfTagNameIncludesSpecial() throws Exception {
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
    void patchShouldReturnCorrectCertificateIfCertificateWasFound() throws Exception {
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
    void patchShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
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
    void patchShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(patch("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void patchShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
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
    void patchShouldThrowExceptionWithCorrectMessageIfNameIsNull() throws Exception {
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
    void patchShouldThrowExceptionWithCorrectMessageIfNameIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", " ")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be empty or blank"));
    }

    @Test
    void patchShouldThrowExceptionWithCorrectMessageIfNameIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(3, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 4 and 40 characters"));
    }

    @Test
    void patchShouldThrowExceptionWithCorrectMessageIfNameIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(41, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 4 and 40 characters"));
    }

    @Test
    void patchShouldThrowExceptionWithCorrectMessageIfNameIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(patch("/api/certificates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(4, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must include only letters and spaces"));
    }

    @Test
    void deleteShouldReturnCorrectMessageIfCertificateWasFound() throws Exception {
        doNothing().when(certificateService).delete(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate was successfully deleted"));
    }

    @Test
    void deleteShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        doThrow(new CustomEntityNotFoundException("failed to find tag by id 1"))
                .when(certificateService).delete(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find tag by id 1"));
    }

    @Test
    void deleteShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void deleteShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }
}