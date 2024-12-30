package com.example.bdcsamsungdevelopertest.domain.info;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrdersProductInfo {

    public record OrdersProduct(
        Long id,
        String name,
        String email,
        List<OrderItem> orders
    ) {
        public OrdersProduct {
            Objects.requireNonNull(id);
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(orders);
        }
    }

    public record OrderItem(
        Long id,
        LocalDateTime orderDate,
        Integer quantity,
        Orders.OrderStatus orderStatus
    ) {
        public OrderItem {
            Objects.requireNonNull(id);
            Objects.requireNonNull(orderDate);
            Objects.requireNonNull(quantity);
            Objects.requireNonNull(orderStatus);
        }
    }
}
