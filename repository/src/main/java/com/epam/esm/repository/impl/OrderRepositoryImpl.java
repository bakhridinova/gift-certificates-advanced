package com.epam.esm.repository.impl;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.QOrder;
import com.epam.esm.entity.User;
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
    public List<Order> findAll(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Order findById(Long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id))
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find order by id " + id));
    }

    @Override
    public List<Order> findByCertificate(Certificate certificate, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .where(qOrder.certificate.eq(certificate))
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public List<Order> findByUser(User user, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .where(qOrder.user.eq(user))
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    public void delete(Order order) {
        // intentionally left blank
    }
}
