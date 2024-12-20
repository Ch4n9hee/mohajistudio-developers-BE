package com.mohajistudio.developers.database.repository.user;

import com.mohajistudio.developers.database.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.mohajistudio.developers.database.entity.QUser.user;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User findByEmailAndPassword(String email, String password) {
        return jpaQueryFactory.selectFrom(user)
                .where(eqEmail(email), eqPassword(password))
                .fetchOne();
    }

    private BooleanExpression eqEmail(String email) {
        if (StringUtils.isNullOrEmpty(email)) return null;
        return user.email.eq(email);
    }

    private BooleanExpression eqPassword(String password) {
        if (StringUtils.isNullOrEmpty(password)) return null;
        return user.password.eq(password);
    }
}
