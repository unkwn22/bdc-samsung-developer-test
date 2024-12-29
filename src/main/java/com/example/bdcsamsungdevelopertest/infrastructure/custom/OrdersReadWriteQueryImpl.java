package com.example.bdcsamsungdevelopertest.infrastructure.custom;

import com.example.bdcsamsungdevelopertest.domain.entity.*;
import com.example.bdcsamsungdevelopertest.domain.query.OrdersQueryEnum;
import com.example.bdcsamsungdevelopertest.infrastructure.custom.expression.MemberQueryExpression;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.OrdersQueryRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrdersReadWriteQueryImpl implements OrdersQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrders qOrders;
    private final QOrderItem qOrderItem;
    private final QMember qMember;

    public OrdersReadWriteQueryImpl(
        JPAQueryFactory jpaQueryFactory,
        MemberQueryExpression expression
    ) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.qOrders = QOrders.orders;
        this.qOrderItem = QOrderItem.orderItem;
        this.qMember = QMember.member;
    }

    /**
     * SELECT o.id as ORDERS_ID
     *      , o.address as ADDRESS
     *      , o.totalAmount as TOTAL_AMOUNT
     *      , m.id as USER_ID
     *      , oi.product_id as PRODUCT_ID
     *      , oi.quantity as QUANTITY
     *   FROM orders o
     *   LEFT JOIN members m ON m.id = o.member_id
     *   LEFT JOIN order_item oi ON oi.order_id = o.id
     *  WHERE 1=1
     *    AND o.id = userId
     *  ORDER BY create_dt DESC
     *  LIMIT 10
     * OFFSET 0
    * */
    @Override
    public List<Tuple> findOrders(Long userId, Pageable pageable) {
        return this.jpaQueryFactory.select(
                    this.qOrders.id.as(Expressions.numberPath(Long.class, OrdersQueryEnum.ORDERS_ID.name())),
                    this.qOrders.address.as(Expressions.stringPath(OrdersQueryEnum.ADDRESS.name())),
                    this.qOrders.totalAmount.as(Expressions.numberPath(Long.class, OrdersQueryEnum.TOTAL_AMOUNT.name())),
                    this.qMember.id.as(Expressions.numberPath(Long.class, OrdersQueryEnum.USER_ID.name())),
                    this.qOrderItem.product.id.as(Expressions.numberPath(Long.class, OrdersQueryEnum.PRODUCT_ID.name())),
                    this.qOrderItem.quantity.as(Expressions.numberPath(Integer.class, OrdersQueryEnum.QUANTITY.name()))
                )
                .from(this.qOrders)
                .leftJoin(this.qMember).on(this.qMember.id.eq(this.qOrders.member.id))
                .leftJoin(this.qOrderItem).on(this.qOrderItem.orders.id.eq(this.qOrders.id))
                .where(this.qMember.id.eq(userId))
                .orderBy(this.qOrders.createDt.desc())
                .limit(pageable.getPageSize())
                .offset((long) pageable.getPageNumber() * pageable.getPageSize())
                .fetch();
    }
}
