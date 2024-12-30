package com.example.bdcsamsungdevelopertest.domain.command;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;

import java.util.Objects;

public record OrdersProductRequestCommand(
    Long productId,
    Orders.OrderStatus orderStatus
) {
    public OrdersProductRequestCommand {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(orderStatus);
    }
}
