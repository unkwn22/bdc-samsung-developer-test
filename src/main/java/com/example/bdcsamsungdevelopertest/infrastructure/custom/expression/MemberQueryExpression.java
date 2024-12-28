package com.example.bdcsamsungdevelopertest.infrastructure.custom.expression;

import com.example.bdcsamsungdevelopertest.domain.entity.QMember;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MemberQueryExpression {

    public OrderSpecifier<LocalDateTime> direction(Pageable pageable) {
        Sort.Direction direction = pageable.getSort()
                .stream()
                .iterator()
                .next()
                .getDirection();
        if(direction.isAscending()) return QMember.member.createDt.asc();
        else return QMember.member.createDt.desc();
    }
}
