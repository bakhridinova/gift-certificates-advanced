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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void getAllByPageShouldReturnEmptyListIfCertificatesWereNotFound() throws Exception {
        when(certificateService.findAllByPage(anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAllByPageShouldReturnCorrectListIfCertificatesWereFound() throws Exception {
        when(certificateService.findAllByPage(anyInt(), anyInt()))
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
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should not be negative"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("size should be of type int"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/certificates")
                        .param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("page must be between 0 and 10000"));
    }

    @Test
    void getAllByPageShouldThrowExceptionWithCorrectMessageIfSizeIsTooBig() throws Exception {
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
        JSONObject jsonObject = new JSONObject();
        when(certificateService.findByFilter(any(), anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject
                        .toString()))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void searchShouldReturnCorrectListIfCertificatesWereFound() throws Exception {
        JSONObject jsonObject = new JSONObject();
        when(certificateService.findByFilter(any(), anyInt(), anyInt()))
                .thenReturn(List.of(getCertificateDto()));
        this.mockMvc.perform(get("/api/certificates/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .toString()))
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
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(get("/api/certificates/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("sortType", " ")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType should not be empty or blank"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortOrderIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(get("/api/certificates/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("sortType", "id")
                                .put("sortOrder", " ")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder should not be empty or blank"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortTypeIsInvalid() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(get("/api/certificates/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                        .put("sortType", "test")
                        .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortType must be either name, price, duration, createdAt or lastUpdatedAt"));
    }

    @Test
    void searchShouldThrowExceptionWithCorrectMessageIfSortOrderIsInvalid() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(get("/api/certificates/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("sortOrder", "test")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("sortOrder must be either asc or desc"));
    }

    @Test
    void getOrdersByPageShouldReturnEmptyListIfOrdersWereNotFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/certificates/1/orders")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getOrdersByPageShouldReturnCorrectListIfOrdersWereFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(getOrderDto()));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
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
    void getOrdersByPageShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        when(orderService.findByCertificateIdAndPage(anyLong(), anyInt(), anyInt()))
                .thenThrow(new CustomEntityNotFoundException("failed to find certificate by id 1"));
        this.mockMvc.perform(get("/api/certificates/1/orders"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find certificate by id 1"));
    }

    @Test
    void getOrdersByPageShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/certificates/text/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void getOrdersByPageShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/certificates/-1/orders"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }

    @Test
    void createShouldReturnCorrectCertificateIfCertificateWasCreated() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfCertificateNameIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDescriptionIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfPriceIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDurationIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagsIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfCertificateNameIsBlank() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDescriptionIsBlank() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfCertificateNameIsTooShort() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDescriptionIsTooShort() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfCertificateNameIsTooLong() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDescriptionIsTooLong() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfCertificateNameIncludesSpecial() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDescriptionIncludesSpecial() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfPriceIsNegative() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDurationIsNegative() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfPriceIsTooLow() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDurationIsNTooLow() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfPriceIsTooHigh() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfDurationIsTooHigh() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagNameIsNull() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagNameIsBlank() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagNameIsTooShort() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagNameIsTooLong() throws Exception {
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
    void createShouldThrowExceptionWithCorrectMessageIfTagNameIncludesSpecial() throws Exception {
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
    void createShouldReturnCorrectCertificateIfCertificateWasFound() throws Exception {
        JSONObject jsonObject = new JSONObject();
        when(certificateService.updateNameById(anyLong(), any()))
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
        when(certificateService.updateNameById(anyLong(), any()))
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(patch("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfNameIsNull() throws Exception {
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfNameIsBlank() throws Exception {
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfNameIsTooShort() throws Exception {
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfNameIsTooLong() throws Exception {
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
    void updateNameByIdShouldThrowExceptionWithCorrectMessageIfNameIncludesSpecial() throws Exception {
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
        doNothing().when(certificateService).deleteById(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message", String.class)
                        .value("certificate was successfully deleted"));
    }

    @Test
    void deleteByIdShouldThrowExceptionWithCorrectMessageIfCertificateWasNotFound() throws Exception {
        doThrow(new CustomEntityNotFoundException("failed to find tag by id 1"))
                .when(certificateService).deleteById(anyLong());
        this.mockMvc.perform(delete("/api/certificates/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find tag by id 1"));
    }

    @Test
    void deleteByIdShouldThrowExceptionWithCorrectMessageIfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void deleteByIdShouldThrowExceptionWithCorrectMessageIfIdIsNegative() throws Exception {
        this.mockMvc.perform(delete("/api/certificates/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }
}