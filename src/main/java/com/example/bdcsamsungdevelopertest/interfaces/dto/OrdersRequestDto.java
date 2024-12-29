package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.List;
import java.util.Objects;

public class OrdersRequestDto {

    public record Orders(
        Long userId,
        String address,
        List<OrderItem> orderItems
    ) {
        public Orders {
            Objects.requireNonNull(userId);
            Objects.requireNonNull(address);
            Objects.requireNonNull(orderItems);
        }
    }

    public record OrderItem(
        Long productId,
        Integer quantity
    ) {
        public OrderItem {
            Objects.requireNonNull(productId);
            Objects.requireNonNull(quantity);
        }
    }
}
