package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomEntityAlreadyExistsException;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.util.TestDataFactory.getTag;
import static com.epam.esm.util.TestDataFactory.getTagDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findAllShouldReturnEmptyListIfNoTagWasFound() {
        when(tagRepository.findAll(any()))
                .thenReturn(List.of());

        assertTrue(tagService
                .findAll(any()).isEmpty());
    }

    @Test
    void findAllShouldReturnCorrectListIfAnyTagWasFound() {
        when(tagRepository.findAll(any()))
                .thenReturn(List.of(getTag()));
        when(tagMapper.toTagDTO(any()))
                .thenReturn(getTagDTO());

        assertEquals(List.of(getTagDTO()),
                tagService.findAll(any()));
    }

    @Test
    void findByIdShouldThrowExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(tagRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> tagService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectTagIfTagWasFound() {
        when(tagRepository.findById(anyLong()))
                .thenReturn(getTag());
        when(tagMapper.toTagDTO(any()))
                .thenReturn(getTagDTO());

        assertEquals(getTagDTO(),
                tagService.findById(anyLong()));
    }

    @Test
    void createShouldThrowExceptionIfTagAlreadyExists() {
        when(tagMapper.toTag(getTagDTO()))
                .thenReturn(getTag());
        when(tagRepository.exists(anyString()))
                .thenReturn(true);

        assertThrows(CustomEntityAlreadyExistsException.class,
                () -> tagService.create(getTagDTO()));
    }

    @Test
    void createShouldReturnCorrectTagIfTagDoesNotExist() {
        when(tagMapper.toTag(any()))
                .thenReturn(getTag());
        when(tagMapper.toTagDTO(any()))
                .thenReturn(getTagDTO());
        when(tagRepository.exists(anyString()))
                .thenReturn(false);

        assertEquals(getTagDTO(), tagService.create(getTagDTO()));
    }

    @Test
    void deleteShouldThrowExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(tagRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> tagService.delete(anyLong()));
    }

    @Test
    void deleteShouldDoNothingIfTagIfTagWasFound() {
        when(tagRepository.findById(anyLong()))
                .thenReturn(getTag());

        assertDoesNotThrow(() -> tagService.delete(anyLong()));
    }
}