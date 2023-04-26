package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDto;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> findAllByPage(int page, int size) {
        Pagination pagination = new Pagination(page, size);
        return userRepository
                .findAllByPage(pagination)
                .stream().map(userMapper::toUserDto).toList();
    }

    @Override
    public UserDto findById(Long id) {
        return userMapper.toUserDto(userRepository.findById(id));
    }
}
