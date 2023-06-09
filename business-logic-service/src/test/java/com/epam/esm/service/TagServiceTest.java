package com.epam.esm.service;

import com.epam.esm.exception.CustomEntityAlreadyExistsException;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.impl.TagServiceImpl;
import com.epam.esm.util.mapper.TagMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.util.TestDataFactory.getTag;
import static com.epam.esm.util.TestDataFactory.getTagDto;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TagServiceTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findAllByPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(tagRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> tagService.findById(anyLong()));
    }

    @Test
    void findAllByPageShouldReturnEmptyListIfNoTagWasFound() {
        when(tagRepository.findAllByPage(any()))
                .thenReturn(List.of());

        assertTrue(tagService
                .findAllByPage(0, 0).isEmpty());
    }

    @Test
    void findAllByPageShouldReturnCorrectListIfAnyTagWasFound() {
        when(tagRepository.findAllByPage(any()))
                .thenReturn(List.of(getTag()));
        when(tagMapper.toTagDto(any()))
                .thenReturn(getTagDto());

        assertEquals(List.of(getTagDto()),
                tagService.findAllByPage(0, 0));
    }

    @Test
    void findByIdShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(tagRepository).findById(anyLong());

        assertThrows(DataAccessException.class,
                () -> tagService.findById(anyLong()));
    }

    @Test
    void findByIdShouldThrowCustomEntityNotFoundExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(tagRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> tagService.findById(anyLong()));
    }

    @Test
    void findByIdShouldReturnCorrectTagIfTagWasFound() {
        when(tagRepository.findById(anyLong()))
                .thenReturn(getTag());
        when(tagMapper.toTagDto(any()))
                .thenReturn(getTagDto());

        assertEquals(getTagDto(),
                tagService.findById(anyLong()));
    }

    @Test
    void createShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        when(tagMapper.toTag(any()))
                .thenReturn(getTag());
        when(tagRepository.findByName(anyString()))
                .thenReturn(Optional.empty());
        doThrow(new DataAccessException("") {})
                .when(tagRepository).save(any());

        assertThrows(DataAccessException.class,
                () -> tagService.create(getTagDto()));
    }

    @Test
    void createShouldThrowCustomEntityNotFoundExceptionIfTagAlreadyExists() {
        when(tagMapper.toTag(getTagDto()))
                .thenReturn(getTag());
        when(tagRepository.findByName(anyString()))
                .thenReturn(Optional.ofNullable(getTag()));

        assertThrows(CustomEntityAlreadyExistsException.class,
                () -> tagService.create(getTagDto()));
    }

    @Test
    void createShouldReturnCorrectTagIfTagDoesNotExist() {
        when(tagMapper.toTag(any()))
                .thenReturn(getTag());
        when(tagMapper.toTagDto(any()))
                .thenReturn(getTagDto());
        when(tagRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        assertEquals(getTagDto(), tagService.create(getTagDto()));
    }

    @Test
    void deleteByPageShouldThrowDataAccessExceptionIfExceptionWasThrown() {
        doThrow(new DataAccessException("") {})
                .when(tagRepository).delete(any());

        assertThrows(DataAccessException.class,
                () -> tagService.deleteById(anyLong()));
    }


    @Test
    void deleteByPageShouldThrowCustomEntityNotFoundExceptionIfNoTagWasFound() {
        doThrow(new CustomEntityNotFoundException(""))
                .when(tagRepository).findById(anyLong());

        assertThrows(CustomEntityNotFoundException.class,
                () -> tagService.deleteById(anyLong()));
    }

    @Test
    void deleteByPageShouldDoNothingIfTagIfTagWasFound() {
        when(tagRepository.findById(anyLong()))
                .thenReturn(getTag());

        assertDoesNotThrow(() -> tagService.deleteById(anyLong()));
    }
}