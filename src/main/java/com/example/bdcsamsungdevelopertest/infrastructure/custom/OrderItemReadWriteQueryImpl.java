package com.example.bdcsamsungdevelopertest.infrastructure.custom;

import com.example.bdcsamsungdevelopertest.domain.command.OrdersProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.*;
import com.example.bdcsamsungdevelopertest.infrastructure.custom.expression.OrderItemQueryExpression;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.OrderItemQueryRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.bdcsamsungdevelopertest.domain.query.OrderItemQueryEnum.*;


@Repository
public class OrderItemReadWriteQueryImpl implements OrderItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QOrders qOrders;
    private final QOrderItem qOrderItem;
    private final QMember qMember;
    private final QProduct qProduct;
    private final OrderItemQueryExpression expression;

    public OrderItemReadWriteQueryImpl(
        JPAQueryFactory jpaQueryFactory,
        OrderItemQueryExpression orderItemQueryExpression
    ) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.qOrders = QOrders.orders;
        this.qOrderItem = QOrderItem.orderItem;
        this.qMember = QMember.member;
        this.qProduct = QProduct.product;
        this.expression = orderItemQueryExpression;
    }

    /**
     * SELECT m.id
     *      , m.name
     *      , m.email
     *      , oi.id
     *      , o.orderDate
     *      , oi.quantity
     *      , o.orderStatus
     *   FROM order_item oi
     *   LEFT JOIN product p ON p.id = oi.product_id
     *   LEFT JOIN orders o ON o.id = oi.orders_id
     *   LEFT JOIN member m ON m.id = o.member_id
     *  WHERE 1=1
     *    AND p.id = {productId}
     *    AND o.orderStatus = {orderStatus}
    * */
    @Override
    public List<Tuple> findOrderItemGroup(OrdersProductRequestCommand searchCommand) {
        return this.jpaQueryFactory.select(
            qMember.id.as(Expressions.numberPath(Long.class, USER_ID.name())),
            qMember.name.as(Expressions.stringPath(NAME.name())),
            qMember.email.as(Expressions.stringPath(EMAIL.name())),
            qOrderItem.id.as(Expressions.numberPath(Long.class, ORDER_ID.name())),
            qOrders.orderDate.as(Expressions.datePath(LocalDateTime.class, ORDER_DATE.name())),
            qOrderItem.quantity.as(Expressions.numberPath(Integer.class, QUANTITY.name())),
            qOrders.orderStatus.as(Expressions.enumPath(Orders.OrderStatus.class, ORDER_STATUS.name()))
        )
        .from(qOrderItem)
        .leftJoin(qProduct).on(qProduct.id.eq(qOrderItem.product.id))
        .leftJoin(qOrders).on(qOrders.id.eq(qOrderItem.orders.id))
        .leftJoin(qMember).on(qMember.id.eq(qOrders.member.id))
        .where(
            expression.eqOrderStatus(searchCommand.orderStatus())
            .and(qProduct.id.eq(searchCommand.productId()))
        ).fetch();
    }
}
