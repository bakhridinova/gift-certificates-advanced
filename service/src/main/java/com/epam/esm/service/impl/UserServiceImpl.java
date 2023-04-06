package com.epam.esm.service.impl;

import com.epam.esm.dto.UserDTO;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
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
    public List<UserDTO> findAll(Pagination pagination) {
        return userRepository
                .findAll(pagination)
                .stream().map(userMapper::toUserDTO).toList();
    }

    @Override
    public UserDTO findById(Long id) {
        return userMapper.toUserDTO(userRepository.findById(id));
    }
}