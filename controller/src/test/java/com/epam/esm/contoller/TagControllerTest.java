package com.epam.esm.contoller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.exception.CustomMessageHolder;
import com.epam.esm.hateoas.HateoasAdder;
import com.epam.esm.service.TagService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.epam.esm.util.TestDataFactory.getTagDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TagController.class)
class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    // beans controllers are dependent on
    @MockBean
    private HateoasAdder<TagDto> tagHateoasAdder;
    @MockBean
    private HateoasAdder<CustomMessageHolder> messageHolderHateoasAdder;

    @Test
    void getAll_ShouldReturn_EmptyList_IfTagsWereNotFound() throws Exception {
        when(tagService.findAll(any()))
                .thenReturn(List.of());
        this.mockMvc.perform(get("/api/tags")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().json("[]"));
    }

    @Test
    void getAll_ShouldReturn_CorrectList_IfTagsWereFound() throws Exception {
        when(tagService.findAll(any()))
                .thenReturn(List.of(getTagDto()));
        this.mockMvc.perform(get("/api/tags"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..id", Long.class).value(0))
                .andExpect(jsonPath("$..name", String.class).value(""));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("page", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("size", String.valueOf(Integer.MIN_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should not be negative"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("page", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("size", "test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size should be of type int"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfPageIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("page", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("page must be between 0 and 10000"));
    }

    @Test
    void getAll_ShouldThrowException_WithCorrectMessage_IfSizeIsTooBig() throws Exception {
        this.mockMvc.perform(get("/api/tags").param("size", String.valueOf(Integer.MAX_VALUE)))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("size must be between 0 and 100"));
    }

    @Test
    void getById_ShouldReturn_CorrectTag_IfTagWasFound() throws Exception {
        when(tagService.findById(anyLong()))
                .thenReturn(getTagDto());
        this.mockMvc.perform(get("/api/tags/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.name", String.class).value(""));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfTagWasNotFound() throws Exception {
        when(tagService.findById(anyLong()))
                .thenThrow(new CustomEntityNotFoundException("failed to find tag by id 1"));
        this.mockMvc.perform(get("/api/tags/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class).value("failed to find tag by id 1"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(get("/api/tags/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id should be of type long"));
    }

    @Test
    void getById_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(get("/api/tags/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class).value("id must be positive"));
    }

    @Test
    void getSpecial_shouldReturn_CorrectTag_ifTagWasFound() throws Exception {
        when(tagService.findSpecial())
                .thenReturn(getTagDto());
        this.mockMvc.perform(get("/api/tags/special"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Long.class).value(0))
                .andExpect(jsonPath("$.name", String.class).value(""));
    }

    @Test
    void post_ShouldReturn_CorrectCertificate_IfTagWasCreated() throws Exception {
        when(tagService.create(any()))
                .thenReturn(getTagDto());
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "test")
                                .toString()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$..name", String.class)
                        .value(""));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIsNull() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", null)
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be null"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIsEmpty() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be empty"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIsBlank() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", " ")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name should not be blank"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIsTooShort() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name", "*")
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 20 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIsTooLong() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(21, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must be between 3 and 20 characters"));
    }

    @Test
    void post_ShouldThrowException_WithCorrectMessage_IfNameIncludesSpecial() throws Exception {
        JSONObject jsonObject = new JSONObject();
        this.mockMvc.perform(post("/api/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject
                                .put("name",
                                        String.join("", Collections.nCopies(3, "*")))
                                .toString()))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("name must include only letters"));
    }

    @Test
    void delete_ShouldReturn_CorrectMessage_IfTagWasFound() throws Exception {
        doNothing().when(tagService).delete(anyLong());
        this.mockMvc.perform(delete("/api/tags/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.message", String.class)
                        .value("tag was successfully deleted"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfTagWasNotFound() throws Exception {
        doThrow(new CustomEntityNotFoundException("failed to find tag by id 1"))
                .when(tagService).delete(anyLong());
        this.mockMvc.perform(delete("/api/tags/1"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", String.class)
                        .value("failed to find tag by id 1"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfIdIsNotNumeric() throws Exception {
        this.mockMvc.perform(delete("/api/tags/test"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id should be of type long"));
    }

    @Test
    void delete_ShouldThrowException_WithCorrectMessage_IfIdIsNegative() throws Exception {
        this.mockMvc.perform(delete("/api/tags/-1"))
                .andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", String.class)
                        .value("id must be positive"));
    }
}