package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class OrderItemInfo {

    public record OrdersEntity(
        Long productId,
        Integer quantity
    ) {
        public OrdersEntity {
            Objects.requireNonNull(productId);
            Objects.requireNonNull(quantity);
        }
    }
}
