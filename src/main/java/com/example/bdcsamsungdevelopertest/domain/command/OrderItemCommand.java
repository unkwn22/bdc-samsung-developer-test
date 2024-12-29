package com.example.bdcsamsungdevelopertest.domain.command;

import java.util.Objects;

public class OrderItemCommand {

    public record OrderItemEntity(
        Long id,
        Integer orderPrice,
        Integer quantity,
        Long orderId,
        Long productId
    ) {
        public OrderItemEntity {
            Objects.requireNonNull(id);
            Objects.requireNonNull(orderPrice);
            Objects.requireNonNull(quantity);
            Objects.requireNonNull(orderId);
            Objects.requireNonNull(productId);
        }
    }
}
