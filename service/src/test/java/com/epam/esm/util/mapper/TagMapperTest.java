package com.epam.esm.util.mapper;

import com.epam.esm.service.config.ServiceTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ServiceTestConfig.class})
class TagMapperTest {
    private final TagMapper tagMapper;

    @Autowired
    public TagMapperTest(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @Test
    void shouldMapTagsCorrectlyTest() {
        assertEquals(getTag(), tagMapper.toTag(getTagDto()));
        assertEquals(getTagDto(), tagMapper.toTagDto(getTag()));
    }

    @Test
    void shouldReturnNullIfNullPassedTest() {
        assertNull(tagMapper.toTag(null));
        assertNull(tagMapper.toTagDto(null));
    }

    @Test
    void shouldReturnNullObjectIfNullObjectPassedTest() {
        assertEquals(getNullTag(), tagMapper.toTag(getNullTagDto()));
        assertEquals(getNullTagDto(), tagMapper.toTagDto(getNullTag()));
    }
}