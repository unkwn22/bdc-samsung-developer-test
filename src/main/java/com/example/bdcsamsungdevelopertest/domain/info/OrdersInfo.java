package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.List;
import java.util.Objects;

public class OrdersInfo {

    public record OrdersEntity(
        Long userId,
        String address,
        Long totalPrice,
        List<OrderItemInfo.OrdersEntity> oderItems
    ) {
        public OrdersEntity {
            Objects.requireNonNull(userId);
            Objects.requireNonNull(address);
            Objects.requireNonNull(totalPrice);
            Objects.requireNonNull(oderItems);
        }
    }
}
