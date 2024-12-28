package com.example.bdcsamsungdevelopertest.infrastructure.custom;

import com.example.bdcsamsungdevelopertest.domain.command.MemberRequestCommand;
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
    private final QMember qMember;

    public MemberReadWriteQueryImpl(
        JPAQueryFactory jpaQueryFactory,
        MemberQueryExpression expression
    ) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.expression = expression;
        this.qMember = QMember.member;
    }

    @Override
    public List<Member> findMembers(Pageable pageable) {
        return this.jpaQueryFactory.selectFrom(this.qMember)
                .orderBy(this.expression.direction(pageable))
                .limit(pageable.getPageSize())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .fetch();
    }

    @Override
    public void updateMember(MemberRequestCommand updateCommand) {
        this.jpaQueryFactory.update(this.qMember)
            .where(
                qMember.id.eq(updateCommand.getId()),
                qMember.email.eq(updateCommand.getEmail())
            )
            .set(this.qMember.name, updateCommand.getName())
            .set(this.qMember.address, updateCommand.getAddress())
            .execute();
    }
}
