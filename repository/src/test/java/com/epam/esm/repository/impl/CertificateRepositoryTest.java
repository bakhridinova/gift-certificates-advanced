package com.epam.esm.repository.impl;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

public class CertificateRepositoryTest extends RepositoryTest {
    private final CertificateRepository certificateRepository;
    @Spy
    private final JPAQueryFactory queryFactory;

    @Autowired
    public CertificateRepositoryTest(EntityManager entityManager,
                                     CertificateRepository certificateRepository) {
        queryFactory = spy(new JPAQueryFactory(entityManager));
        this.certificateRepository = certificateRepository;
    }

    private Certificate certificate;

    private Pagination pagination;

    @BeforeEach
    void setUp() {
        certificate = new Certificate();
        certificate.setName("");
        certificate.setDescription("");
        certificate.setPrice(0.0);
        certificate.setDuration(0);
        certificate.setOrders(List.of());
        certificate.setTags(Set.of());

        pagination = new Pagination(0, 10);
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
        doThrow(PersistenceException.class)
                .when(queryFactory).selectFrom(any());

        assertThrows(DataAccessException.class,
                () -> certificateRepository.findAll(pagination));
    }

    @Test
    @Order(5)
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
    @Order(7)
    public void findByFilterShouldReturnEmptyListInitially() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("").description("")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .build();

        assertTrue(certificateRepository
                .findByFilter(searchFilter).isEmpty());
    }

    @Test
    @Order(8)
    public void findByFilterShouldReturnCorrectListIfCertificateWasFound() {
        certificateRepository.save(certificate);

        SearchFilter searchFilter = SearchFilter.builder()
                .name("").description("")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .build();


        assertEquals(1, certificateRepository
                .findByFilter(searchFilter).size());

        assertEquals(certificate,  certificateRepository
                .findByFilter(searchFilter).get(0));
    }

    @Test
    @Order(9)
    public void findByFilterShouldReturnEmptyListIfCertificateNamesDontMatch() {
        certificateRepository.save(certificate);

        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .build();

        assertEquals(0, certificateRepository
                .findByFilter(searchFilter).size());
    }

    @Test
    @Order(10)
    public void findByFilterShouldReturnEmptyListIfCertificateDescriptionsDontMatch() {
        certificateRepository.save(certificate);

        SearchFilter searchFilter = SearchFilter.builder()
                .name("").description("test")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .build();

        assertEquals(0, certificateRepository
                .findByFilter(searchFilter).size());
    }

    @Test
    @Order(10)
    public void findByFilterShouldReturnEmptyListIfCertificateTagsDontMatch() {
        certificateRepository.save(certificate);

        Tag tag = new Tag();
        tag.setName("test");
        SearchFilter searchFilter = SearchFilter.builder()
                .name("").description("")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .tags(Set.of(tag))
                .build();

        assertEquals(0, certificateRepository
                .findByFilter(searchFilter).size());
    }

    @Test
    @Order(10)
    public void findByFilterShouldThrowDatAccessExceptionIfExceptionWasThrown() {
        doThrow(PersistenceException.class)
                .when(queryFactory).selectFrom(any());

        SearchFilter searchFilter = SearchFilter.builder()
                .name("").description("")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .build();

        assertThrows(DataAccessException.class,
                () -> certificateRepository.findByFilter(searchFilter));
    }

    @Test
    @Order(11)
    public void saveShouldUpdateCertificateCorrectly() {

    }
}