package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.QOrder;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final EntityManager entityManager;

    @Override
    public List<Order> findAllByPage(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;

        return queryFactory.selectFrom(qOrder)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Long findTotalNumber() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;

        return queryFactory.select(qOrder.count())
                .from(qOrder).fetchFirst();
    }

    @Override
    public Order findById(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;

        return Optional.ofNullable(queryFactory.selectFrom(qOrder)
                        .where(qOrder.id.eq(id)).fetchFirst())
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find order by id " + id));
    }

    @Override
    public List<Order> findByCertificateId(Long certificateId, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .where(qOrder.certificate.id.eq(certificateId))
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public List<Order> findByUserId(Long userId, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .where(qOrder.user.id.eq(userId))
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }
}
