package com.epam.esm.repository.impl;

import com.epam.esm.entity.QCertificate;
import com.epam.esm.entity.QOrder;
import com.epam.esm.entity.QTag;
import com.epam.esm.entity.QUser;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
    private final EntityManager entityManager;

    @Override
    public List<Tag> findAll(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QTag qTag = QTag.tag;
        return queryFactory.selectFrom(qTag)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Tag findById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id))
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find tag by id " + id));
    }



    @Override
    public Tag findSpecial() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCertificate qCertificate = QCertificate.certificate;
        QOrder qOrder = QOrder.order;
        QUser qUser = QUser.user;
        QTag qTag = QTag.tag;

        return queryFactory
                .selectFrom(qTag)
                .where(qTag.id.eq(queryFactory
                        .select(qTag.id)
                        .from(qCertificate)
                        .innerJoin(qTag)
                        .where(qCertificate.in(queryFactory
                                .select(qCertificate)
                                .from(qOrder)
                                .innerJoin(qOrder.certificate, qCertificate)
                                .where(qOrder.user.eq(queryFactory
                                        .select(qUser)
                                        .from(qOrder)
                                        .innerJoin(qOrder.user, qUser)
                                        .groupBy(qOrder.user)
                                        .orderBy(qOrder.price.sum().desc())
                                        .fetchFirst()))
                                .distinct()))
                        .groupBy(qTag.id)
                        .orderBy(qTag.id.count().desc())
                        .fetchFirst()))
                .fetchFirst();
    }

    @Override
    public boolean exists(String name) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QTag qTag = QTag.tag;
        return queryFactory.selectFrom(qTag)
                .where(qTag.name.eq(name))
                .fetchOne() != null;
    }

    @Override
    @Transactional
    public void save(Tag tag) {
        entityManager.persist(tag);
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }
}
