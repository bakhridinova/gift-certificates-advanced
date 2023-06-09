package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityAlreadyExistsException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagDto> findAllByPage(int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return tagRepository.findAllByPage(pagination)
                .stream().map(tagMapper::toTagDto).toList();
    }

    @Override
    public TagDto findById(Long id) {
        return tagMapper.toTagDto(tagRepository.findById(id));
    }

    @Override
    public TagDto findSpecial() {
        return tagMapper.toTagDto(tagRepository.findSpecial());
    }

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = tagMapper.toTag(tagDto);
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new CustomEntityAlreadyExistsException("tag with such name already exists");
        }

        tagRepository.save(tag);
        return tagMapper.toTagDto(tag);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        tagRepository.delete(tagRepository.findById(id));
    }
}
