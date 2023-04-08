package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Certificate;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.JDBCException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;

import static com.epam.esm.util.TestDataFactory.getPagination;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class CertificateRepositoryTest extends RepositoryTest {

    @Autowired
    private CertificateRepository certificateRepository;

    @Mock
    private static JPAQueryFactory queryFactory;

    private static Pagination pagination;

    private static SearchFilter filter;

    private static Certificate certificate;

    @BeforeAll
    static void setUp() {
        pagination = getPagination(0, 10);
        queryFactory  = mock(JPAQueryFactory.class);
        filter = new SearchFilter("", "", "id", "desc", Set.of(), Set.of(), pagination);
    }

    @BeforeEach
    void setUser() {
        certificate = new Certificate();
        certificate.setName("");
        certificate.setDescription("");
        certificate.setPrice(0.0);
        certificate.setDuration(0);
        certificate.setOrders(List.of());
        certificate.setTags(Set.of());
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(certificateRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnEmptyListInitially() {
        assertTrue(certificateRepository
                .findAll(pagination).isEmpty());
    }

    @Test
    @Order(3)
    public void findAllShouldReturnCorrectListAfterSaving() {
        certificateRepository.save(certificate);

        assertEquals(1, certificateRepository
                .findAll(pagination).size());

        assertEquals(certificate, certificateRepository
                .findAll(pagination).get(0));
    }

    @Test
    @Order(4)
    public void findAllShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(JDBCException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> certificateRepository.findAll(pagination));
    }

    @Test
    @Order(6)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        certificateRepository.save(certificate);
        assertEquals(certificate, certificateRepository.findById(2L));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateRepository.findById(1L));
    }

    @Test
    public void findByFilterShouldReturnEmptyListInitially() {
        assertTrue(certificateRepository
                .findByFilter(filter).isEmpty());
    }

    @Test
    public void findByFilterShouldReturnCorrectListAfterSaving() {
        certificateRepository.save(certificate);

        assertEquals(1, certificateRepository
                .findByFilter(filter).size());

        assertEquals(certificate,  certificateRepository
                .findByFilter(filter).get(0));
    }

    @Test
    public void findByFilterShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(JDBCException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> certificateRepository.findByFilter(filter));
    }

    @Test
    public void saveShouldUpdateCertificateCorrectly() {

    }
}