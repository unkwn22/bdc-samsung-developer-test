package com.example.bdcsamsungdevelopertest.infrastructure.custom.expression;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.entity.QOrders;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderItemQueryExpression {

    public BooleanExpression eqOrderStatus(Orders.OrderStatus orderStatus) {
        return switch (orderStatus) {
            case ORDERED -> QOrders.orders.orderStatus.eq(Orders.OrderStatus.ORDERED);
            case CANCELLED -> QOrders.orders.orderStatus.eq(Orders.OrderStatus.CANCELLED);
            case ALL -> QOrders.orders.orderStatus.in(List.of(Orders.OrderStatus.ORDERED, Orders.OrderStatus.CANCELLED));
        };
    }
}
