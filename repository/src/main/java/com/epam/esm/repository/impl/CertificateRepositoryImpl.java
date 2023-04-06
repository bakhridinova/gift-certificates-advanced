package com.epam.esm.repository.impl;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.dto.extra.SearchFilter;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.QCertificate;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateRepositoryImpl implements CertificateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private static final Map<String, Comparator<Certificate>> certificateComparators = Map.of(
            "name", Comparator.comparing(Certificate::getName),
            "description", Comparator.comparing(Certificate::getDescription),
            "price", Comparator.comparing(Certificate::getPrice),
            "duration", Comparator.comparing(Certificate::getDuration),
            "createdAt", Comparator.comparing(Certificate::getCreatedAt),
            "lastUpdatedAt", Comparator.comparing(Certificate::getLastUpdatedAt)
    );

    @Override
    public List<Certificate> findAll(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;
        return queryFactory.selectFrom(qCertificate)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Certificate findById(Long id) {
        return Optional.ofNullable(entityManager.find(Certificate.class, id))
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find certificate by id " + id));
    }

    @Override
    public List<Certificate> findByFilter(SearchFilter searchFilter) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;

        List<Certificate> certificates = new ArrayList<>(
                queryFactory.selectFrom(qCertificate)
                .where(qCertificate.name.contains(searchFilter.name()))
                .where(qCertificate.description.contains(searchFilter.description()))
                .stream().filter(c -> c.getTags().containsAll(searchFilter.tags()))
                .skip(searchFilter.getSkip()).limit(searchFilter.getLimit())
                .toList());

        Comparator<Certificate> comparator = certificateComparators.getOrDefault(
                searchFilter.sortType(), Comparator.comparing(Certificate::getId));
        certificates.sort(comparator);
        if (searchFilter.isDescending()) {
            Collections.reverse(certificates);
        }
        return certificates;
    }

    @Override
    public void save(Certificate certificate) {
        entityManager.persist(certificate);
    }

    @Override
    public void delete(Certificate certificate) {
        entityManager.remove(certificate);
    }
}