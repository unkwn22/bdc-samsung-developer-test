package com.example.bdcsamsungdevelopertest.infrastructure.custom;

import com.example.bdcsamsungdevelopertest.domain.entity.Member;
import com.example.bdcsamsungdevelopertest.domain.entity.QMember;
import com.example.bdcsamsungdevelopertest.infrastructure.custom.expression.MemberQueryExpression;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.MemberQueryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberReadWriteQueryImpl implements MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final MemberQueryExpression expression;

    public MemberReadWriteQueryImpl(
        JPAQueryFactory jpaQueryFactory,
        MemberQueryExpression expression
    ) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.expression = expression;
    }

    @Override
    public List<Member> findMembers(Pageable pageable) {
        QMember qMember = QMember.member;
        return this.jpaQueryFactory.selectFrom(qMember)
                .orderBy(this.expression.direction(pageable))
                .limit(pageable.getPageSize())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .fetch();
    }
}
