package com.epam.esm.repository.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.QCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.util.Pagination;
import com.epam.esm.util.SearchFilter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CertificateRepositoryImpl implements CertificateRepository {
    private final EntityManager entityManager;

    private static final Map<String, Comparator<Certificate>> certificateComparators = Map.of(
            "id", Comparator.comparing(Certificate::getId),
            "name", Comparator.comparing(Certificate::getName),
            "description", Comparator.comparing(Certificate::getDescription),
            "price", Comparator.comparing(Certificate::getPrice),
            "duration", Comparator.comparing(Certificate::getDuration),
            "createdAt", Comparator.comparing(Certificate::getCreatedAt),
            "lastUpdatedAt", Comparator.comparing(Certificate::getLastUpdatedAt)
    );

    @Override
    public List<Certificate> findAllByPage(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;

        return queryFactory.selectFrom(qCertificate)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Certificate findById(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;

        return Optional.ofNullable(queryFactory.selectFrom(qCertificate)
                        .where(qCertificate.id.eq(id)).fetchFirst())
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find certificate by id " + id));
    }

    @Override
    public List<Certificate> findByFilterAndPage(SearchFilter searchFilter, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;

        List<Certificate> certificates = new ArrayList<>(
                queryFactory.selectFrom(qCertificate)
                .where(qCertificate.name.contains(searchFilter.name()))
                .where(qCertificate.description.contains(searchFilter.description()))
                .stream().filter(c -> c.getTags().stream().map(Tag::getName).collect(Collectors.toSet()).containsAll(
                        searchFilter.tags().stream().map(TagDto::getName).collect(Collectors.toSet())))
                .skip(pagination.getOffset()).limit(pagination.getLimit())
                .toList());

        certificates.sort(certificateComparators.get(searchFilter.sortType()));
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