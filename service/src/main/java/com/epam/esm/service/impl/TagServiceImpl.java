package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityAlreadyExistsException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.util.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public List<TagDTO> findAll(Pagination pagination) {
        return tagRepository.findAll(pagination)
                .stream().map(tagMapper::toTagDTO).toList();
    }

    @Override
    public TagDTO findById(Long id) {
        return tagMapper.toTagDTO(tagRepository.findById(id));
    }

    @Override
    public TagDTO findSpecial() {
        return tagMapper.toTagDTO(tagRepository.findMostWidelyUsedTagOfAUserWithTheHighestCostOfAllOrders());
    }

    public TagDTO create(TagDTO tagDTO) {
        Tag tag = tagMapper.toTag(tagDTO);
        if (tagRepository.exists(tag.getName())) {
            throw new CustomEntityAlreadyExistsException("tag with such name already exists");
        }

        tagRepository.save(tag);
        return tagMapper.toTagDTO(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.delete(tagRepository.findById(id));
    }
}
