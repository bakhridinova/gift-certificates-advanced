package com.epam.esm.util;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

/**
 * utility class holding static entities for tests
 *
 * @author bakhridinova
 */

@UtilityClass
public class TestDataFactory {
    private final Tag TAG;
    private final TagDto TAG_DTO;
    private final Tag NULL_TAG;
    private final TagDto NULL_TAG_DTO;
    private final User USER;
    private final UserDto USER_DTO;
    private final User NULL_USER;
    private final UserDto NULL_USER_DTO;
    private final Order ORDER;
    private final OrderDto ORDER_DTO;
    private final Order NULL_ORDER;
    private final OrderDto NULL_ORDER_DTO;
    private final Certificate CERTIFICATE;
    private final CertificateDto CERTIFICATE_DTO;
    private final Certificate NULL_CERTIFICATE;
    private final CertificateDto NULL_CERTIFICATE_DTO;
    private final Pagination PAGINATION = new Pagination(0, 0);

    static {

        TAG = Tag.builder().id(0L).name("").build();
        TAG_DTO = TagDto.builder().id(0L).name("").build();

        NULL_TAG = Tag.builder().build();
        NULL_TAG_DTO = TagDto.builder().build();

        USER = User.builder().id(0L).username("").password("")
                .firstName("").lastName("").emailAddress("").orders(List.of()).build();
        USER_DTO = UserDto.builder().id(0L).username("").password("")
                .firstName("").lastName("").emailAddress("").build();

        NULL_USER = User.builder().orders(List.of()).build();
        NULL_USER_DTO = UserDto.builder().build();

        CERTIFICATE = Certificate.builder().id(0L).name("").description("")
                .price(0.0).duration(0).user(USER).tags(Set.of()).orders(List.of()).build();
        CERTIFICATE_DTO = CertificateDto.builder().id(0L).name("").description("")
                .price(0.0).duration(0).userId(USER.getId()).tags(Set.of()).build();

        NULL_CERTIFICATE = Certificate.builder()
                .tags(Set.of()).user(USER).orders(List.of()).build();
        NULL_CERTIFICATE_DTO = CertificateDto.builder()
                .tags(Set.of()).userId(USER.getId()).build();

        ORDER = Order.builder().id(0L).price(CERTIFICATE.getPrice())
                .user(USER).certificate(CERTIFICATE).build();
        ORDER_DTO =  OrderDto.builder().id(0L).price(CERTIFICATE.getPrice())
                .userId(USER.getId()).certificateId(CERTIFICATE.getId()).build();

        NULL_ORDER = Order.builder()
                .user(USER).certificate(CERTIFICATE).build();
        NULL_ORDER_DTO = OrderDto.builder()
                .userId(USER.getId()).certificateId(CERTIFICATE.getId()).build();
    }

    // entity

    public Certificate getCertificate() {
        return CERTIFICATE;
    }

    public Order getOrder() {
        return ORDER;
    }

    public Tag getTag() {
        return TAG;
    }

    public User getUser() {
        return USER;
    }

    // entity dto

    public CertificateDto getCertificateDto() {
        return CERTIFICATE_DTO;
    }

    public OrderDto getOrderDto() {
        return ORDER_DTO;
    }

    public TagDto getTagDto() {
        return TAG_DTO;
    }

    public UserDto getUserDto() {
        return USER_DTO;
    }

    // null entity

    public Certificate getNullCertificate() {
        return NULL_CERTIFICATE;
    }

    public Order getNullOrder() {
        return NULL_ORDER;
    }

    public Tag getNullTag() {
        return NULL_TAG;
    }

    public User getNullUser() {
        return NULL_USER;
    }

    // null entity dto

    public CertificateDto getNullCertificateDto() {
        return NULL_CERTIFICATE_DTO;
    }

    public OrderDto getNullOrderDto() {
        return NULL_ORDER_DTO;
    }

    public TagDto getNullTagDto() {
        return NULL_TAG_DTO;
    }

    public UserDto getNullUserDto() {
        return NULL_USER_DTO;
    }

    public Pagination getPagination() {
        return PAGINATION;
    }

    public Pagination getPagination(int page, int size) {
        return new Pagination(page, size);
    }
}
