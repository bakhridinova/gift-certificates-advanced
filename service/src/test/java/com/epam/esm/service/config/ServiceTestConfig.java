package com.epam.esm.service.config;

import com.epam.esm.util.mapper.CertificateMapper;
import com.epam.esm.util.mapper.OrderMapper;
import com.epam.esm.util.mapper.TagMapper;
import com.epam.esm.util.mapper.UserMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceTestConfig {
    @Bean
    public CertificateMapper certificateMapper() {
        return Mappers.getMapper(CertificateMapper.class);
    }

    @Bean
    public OrderMapper orderMapper() {
        return Mappers.getMapper(OrderMapper.class);
    }

    @Bean
    public TagMapper tagMapper() {
        return Mappers.getMapper(TagMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return Mappers.getMapper(UserMapper.class);
    }
}
