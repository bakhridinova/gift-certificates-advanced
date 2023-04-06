package com.epam.esm.repository.impl;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.QOrder;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.OrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

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
                .stream().toList();
    }

    @Override
    public List<Order> findByUser(User user, Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QOrder qOrder = QOrder.order;
        return queryFactory.selectFrom(qOrder)
                .where(qOrder.user.eq(user))
                .stream().toList();
    }

    @Override
    public void save(Order order) {
        entityManager.persist(order);
    }

    @Override
    public void delete(Order order) {

    }
}
