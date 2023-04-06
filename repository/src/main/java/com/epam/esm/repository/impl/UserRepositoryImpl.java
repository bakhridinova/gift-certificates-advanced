package com.epam.esm.repository.impl;

import com.epam.esm.dto.extra.Pagination;
import com.epam.esm.entity.QUser;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;
        return queryFactory.selectFrom(qUser)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public User findById(Long id) {
        return Optional.ofNullable(entityManager.find(User.class, id))
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find user by id " + id));
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }
}
