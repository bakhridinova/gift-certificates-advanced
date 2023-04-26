package com.epam.esm.util.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * mapper to convert Order into OrderDto and vice versa
 *
 * @author bakhridinova
 */

@Mapper(componentModel = "spring")
public interface OrderMapper {
    /**
     * maps Order to OrderDto
     * sets id of user to userId field
     * sets id of certificate to certificateId field
     *
     * @param order Order
     * @return OrderDto
     */
    @Mapping(target = "userId", expression = "java(order.getUser().getId())")
    @Mapping(target = "certificateId", expression = "java(order.getCertificate().getId())")
    OrderDto toOrderDto(Order order);
}
