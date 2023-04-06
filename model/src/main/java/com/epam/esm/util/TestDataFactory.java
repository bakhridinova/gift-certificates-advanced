package com.epam.esm.util;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * utility class holding static entities for tests
 *
 * @author bakhridinova
 */

@UtilityClass
public class TestDataFactory {
    private final Tag TAG = new Tag();
    private final TagDto TAG_DTO = new TagDto();
    private final Tag NULL_TAG = new Tag();
    private final TagDto NULL_TAG_DTO = new TagDto();
    private final User USER = new User();
    private final UserDto USER_DTO = new UserDto();
    private final User NULL_USER = new User();
    private final UserDto NULL_USER_DTO = new UserDto();
    private final Order ORDER = new Order();
    private final OrderDto ORDER_DTO = new OrderDto();
    private final Order NULL_ORDER = new Order();
    private final OrderDto NULL_ORDER_DTO = new OrderDto();
    private final Certificate CERTIFICATE = new Certificate();
    private final CertificateDto CERTIFICATE_DTO = new CertificateDto();
    private final Certificate NULL_CERTIFICATE = new Certificate();
    private final CertificateDto NULL_CERTIFICATE_DTO = new CertificateDto();
    private final Pagination PAGINATION = new Pagination(0, 0);

    static {
        LocalDateTime now = LocalDateTime.now();

        TAG.setId(0L);                           TAG_DTO.setId(0L);
        TAG.setName("");                         TAG_DTO.setName("");

        USER.setId(0L);                          USER_DTO.setId(0L);
        USER.setUsername("");                    USER_DTO.setUsername("");
        USER.setPassword("");                    USER_DTO.setPassword("");
        USER.setFirstName("");                   USER_DTO.setFirstName("");
        USER.setLastName("");                    USER_DTO.setLastName("");
        USER.setEmailAddress("");                USER_DTO.setEmailAddress("");
        USER.setOrders(List.of());               USER_DTO.setTimesOrdered(0);
        NULL_USER.setOrders(List.of());          NULL_USER_DTO.setTimesOrdered(0);

        CERTIFICATE.setId(0L);                   CERTIFICATE_DTO.setId(0L);
        CERTIFICATE.setName("");                 CERTIFICATE_DTO.setName("");
        CERTIFICATE.setDescription("");          CERTIFICATE_DTO.setDescription("");
        CERTIFICATE.setPrice(0.0);               CERTIFICATE_DTO.setPrice(0.0);
        CERTIFICATE.setDuration(0);              CERTIFICATE_DTO.setDuration(0);
        CERTIFICATE.setCreatedAt(now);           CERTIFICATE_DTO.setCreatedAt(now);
        CERTIFICATE.setLastUpdatedAt(now);       CERTIFICATE_DTO.setLastUpdatedAt(now);
        CERTIFICATE.setOrders(List.of());        CERTIFICATE_DTO.setTags(Set.of());
        CERTIFICATE.setTags(Set.of());           CERTIFICATE_DTO.setTimesOrdered(0);
        NULL_CERTIFICATE.setTags(Set.of());      NULL_CERTIFICATE_DTO.setTags(Set.of());
        NULL_CERTIFICATE.setOrders(List.of());   NULL_CERTIFICATE_DTO.setTimesOrdered(0);

        ORDER.setId(0L);                          ORDER_DTO.setId(0L);
        ORDER.setPrice(0.0);                      ORDER_DTO.setPrice(0.0);
        ORDER.setCreatedAt(now);                  ORDER_DTO.setCreatedAt(now);
        ORDER.setUser(USER);                      ORDER_DTO.setUserId(0L);
        ORDER.setCertificate(CERTIFICATE);        ORDER_DTO.setCertificateId(0L);
        NULL_ORDER.setUser(USER);                 NULL_ORDER_DTO.setUserId(0L);
        NULL_ORDER.setCertificate(CERTIFICATE);   NULL_ORDER_DTO.setCertificateId(0L);
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
}
