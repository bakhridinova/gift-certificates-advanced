package com.epam.esm.repository.impl;

import com.epam.esm.entity.QUser;
import com.epam.esm.entity.User;
import com.epam.esm.exception.CustomEntityNotFoundException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.util.Pagination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;

    @Override
    public List<User> findAllByPage(Pagination pagination) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;

        return queryFactory.selectFrom(qUser)
                .offset(pagination.getOffset())
                .limit(pagination.getLimit())
                .stream().toList();
    }

    @Override
    public Long findTotalNumber() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;

        return queryFactory.select(qUser.count())
                .from(qUser).fetchFirst();
    }

    @Override
    public User findById(Long id) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QUser qUser = QUser.user;

        return Optional.ofNullable(queryFactory.selectFrom(qUser)
                        .where(qUser.id.eq(id)).fetchFirst())
                .orElseThrow(() -> new CustomEntityNotFoundException(
                        "failed to find user by id " + id));
    }
}
