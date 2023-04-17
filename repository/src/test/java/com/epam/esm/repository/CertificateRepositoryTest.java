package com.epam.esm.repository;

import com.epam.esm.RepositoryTest;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CertificateRepositoryTest extends RepositoryTest {
    @Autowired
    private CertificateRepository certificateRepository;
    private static Pagination pagination;

    @BeforeAll
    static void setUp() {
        pagination = new Pagination(0, 10);
    }

    @Test
    @Order(1)
    public void contextLoads() {
        assertNotNull(certificateRepository);
    }

    @Test
    @Order(2)
    public void findAllShouldReturnInitialList() {
        assertEquals(10, certificateRepository
                .findAll(pagination).size());

        AtomicInteger counter = new AtomicInteger(1);
        certificateRepository.findAll(pagination)
                .forEach((certificate) -> {
                    int value = counter.get();
                    assertEquals(value,
                            certificate.getId());
                    assertEquals("test" + value,
                            certificate.getName());
                    assertEquals("test" + value,
                            certificate.getDescription());
                    assertEquals(value,
                            certificate.getPrice());
                    assertEquals(value,
                            certificate.getDuration());
                    counter.incrementAndGet();
                });
    }

    @Test
    @Order(3)
    public void findTotalNumberShouldReturnInitialLong() {
        assertEquals(10, certificateRepository.findTotalNumber());
    }

    @Test
    @Order(4)
    public void findByIdShouldReturnCorrectCertificateIfCertificateExists() {
        Certificate certificate = certificateRepository.findById(1L);
        assertEquals(1,
                certificate.getId());
        assertEquals("test1",
                certificate.getName());
        assertEquals("test1",
                certificate.getDescription());
        assertEquals(1,
                certificate.getPrice());
        assertEquals(1,
                certificate.getDuration());
    }

    @Test
    @Order(5)
    public void findByIdShouldThrowCustomEntityNotFoundExceptionIfCertificateDoesNotExist() {
        assertThrows(CustomEntityNotFoundException.class,
                () -> certificateRepository.findById(Long.MAX_VALUE));
    }

    @Test
    @Order(6)
    public void findByIdShouldThrowDataAccessExceptionIfCertificateIdIsNull() {
        assertThrows(DataAccessException.class,
                () -> certificateRepository.findById(null));
    }

    @Test
    @Order(7)
    public void findByFilterShouldReturnInitialListInAscendingIdOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("id").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getId()
                        .compareTo(certificates.get(i).getId()) < 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(8)
    public void findByFilterShouldReturnInitialListInDescendingIdOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("id").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getId()
                        .compareTo(certificates.get(i).getId()) > 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(9)
    public void findByFilterShouldReturnInitialListInAscendingNamedOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("id").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getId()
                        .compareTo(certificates.get(i).getId()) < 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(10)
    public void findByFilterShouldReturnInitialListInDescendingNameOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("name").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getName()
                        .compareTo(certificates.get(i).getName()) > 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(11)
    public void findByFilterShouldReturnInitialListInAscendingDescriptionOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("description").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getDescription()
                        .compareTo(certificates.get(i).getDescription()) < 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(12)
    public void findByFilterShouldReturnInitialListInDescendingDescriptionOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("description").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getDescription()
                        .compareTo(certificates.get(i).getDescription()) > 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(13)
    public void findByFilterShouldReturnInitialListInAscendingPriceOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("price").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getPrice()
                        .compareTo(certificates.get(i).getPrice()) < 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(14)
    public void findByFilterShouldReturnInitialListInDescendingPriceOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("price").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getPrice()
                        .compareTo(certificates.get(i).getPrice()) > 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(15)
    public void findByFilterShouldReturnInitialListInAscendingDurationOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("duration").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getDuration()
                        .compareTo(certificates.get(i).getDuration()) < 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(16)
    public void findByFilterShouldReturnInitialListInDescendingDurationOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("duration").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> certificates.get(i - 1).getDuration()
                        .compareTo(certificates.get(i).getDuration()) > 0)
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(17)
    public void findByFilterShouldReturnInitialListInAscendingCreatedAtOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("createdAt").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> !certificates.get(i - 1).getCreatedAt()
                        .isAfter(certificates.get(i).getCreatedAt()))
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(18)
    public void findByFilterShouldReturnInitialListInDescendingCreatedAtOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("createdAt").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> !certificates.get(i - 1).getCreatedAt()
                        .isBefore(certificates.get(i).getCreatedAt()))
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(19)
    public void findByFilterShouldReturnInitialListInAscendingLastUpdatedAtOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("lastUpdatedAt").sortOrder("asc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> !certificates.get(i - 1).getLastUpdatedAt()
                        .isAfter(certificates.get(i).getLastUpdatedAt()))
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(20)
    public void findByFilterShouldReturnInitialListInDescendingLastUpdatedAtOrder() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test")
                .sortType("lastUpdatedAt").sortOrder("desc")
                .pagination(pagination).build();

        List<Certificate> certificates =
                certificateRepository.findByFilter(searchFilter);

        IntStream.range(1, certificates.size())
                .mapToObj(i -> !certificates.get(i - 1).getLastUpdatedAt()
                        .isBefore(certificates.get(i).getLastUpdatedAt()))
                .forEach(Assertions::assertTrue);
    }

    @Test
    @Order(21)
    public void findByFilterShouldReturnEmptyListIfNamesDoNotMatch() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test name").description("test")
                .sortType("id").sortOrder("desc")
                .pagination(pagination).build();

        assertTrue(certificateRepository
                .findByFilter(searchFilter).isEmpty());
    }

    @Test
    @Order(22)
    public void findByFilterShouldReturnEmptyListIfDescriptionsDoNotMatch() {
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test description")
                .sortType("id").sortOrder("desc")
                .pagination(pagination).build();

        assertTrue(certificateRepository
                .findByFilter(searchFilter).isEmpty());
    }

    @Test
    @Order(23)
    public void findByFilterShouldReturnEmptyListIfTagsDoNotMatch() {
        Tag tag = Tag.builder()
                .name("test name").build();
        SearchFilter searchFilter = SearchFilter.builder()
                .name("test").description("test description")
                .sortType("id").sortOrder("desc")
                .pagination(pagination)
                .tags(Set.of(tag)).build();

        assertTrue(certificateRepository
                .findByFilter(searchFilter).isEmpty());
    }

    @Test
    @Order(24)
    public void saveShouldAddNewRecordToDataBase() {
        Certificate certificate = Certificate.builder().name("").description("")
                .price(0.0).duration(0).tags(Set.of()).orders(List.of()).build();
        certificateRepository.save(certificate);
        assertEquals(certificate,
                certificateRepository.findById(11L));
    }

    @Test
    @Order(25)
    public void saveShouldThrowDataAccessExceptionIfNameIsNull() {
        Certificate certificate = Certificate.builder().build();
        assertThrows(DataAccessException.class,
                () -> certificateRepository.save(certificate));
    }

    @Test
    @Order(26)
    public void saveShouldThrowDataAccessExceptionIfDescriptionIsNull() {
        Certificate certificate = Certificate.builder()
                .name("").build();
        assertThrows(DataAccessException.class,
                () -> certificateRepository.save(certificate));
    }

    @Test
    @Order(27)
    public void saveShouldThrowDataAccessExceptionIfPriceIsNull() {
        Certificate certificate = Certificate.builder()
                .name("").description("").build();
        assertThrows(DataAccessException.class,
                () -> certificateRepository.save(certificate));
    }

    @Test
    @Order(28)
    public void saveShouldThrowDataAccessExceptionIfDurationIsNull() {
        Certificate certificate = Certificate.builder()
                .name("").description("").price(0.0).build();
        assertThrows(DataAccessException.class,
                () -> certificateRepository.save(certificate));
    }

    @Test
    @Order(29)
    public void deleteShouldRemoveRecordFromDatabase() {
        Certificate certificate = Certificate.builder().name("").description("")
                .price(0.0).duration(0).tags(Set.of()).orders(List.of()).build();
        certificateRepository.save(certificate);
        assertEquals(1,
                certificateRepository.findAll(pagination.next()).size());
        certificateRepository.delete(certificate);
        assertEquals(0,
                certificateRepository.findAll(pagination.next()).size());
    }

    @Test
    @Order(30)
    public void deleteShouldThrowDataAccessExceptionIfEntityIsAlreadyDetached() {
        Certificate certificate = Certificate.builder().name("").description("")
                .price(0.0).duration(0).tags(Set.of()).orders(List.of()).build();
        certificateRepository.save(certificate);
        assertEquals(1,
                certificateRepository.findAll(pagination.next()).size());
        certificateRepository.delete(certificate);
        assertEquals(0,
                certificateRepository.findAll(pagination.next()).size());
        assertThrows(DataAccessException.class,
                () -> certificateRepository.delete(certificate));
    }
}