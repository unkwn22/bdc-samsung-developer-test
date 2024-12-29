package com.example.bdcsamsungdevelopertest.domain.command;

import java.util.List;
import java.util.Objects;

public class OrdersCommand {

    public record OrdersEntity(
        Long id,
        MemberCommand.MemberEntity memberCommand,
        Long totalAmount,
        String address,
        List<OrderItemCommand.OrderItemEntity> ordersItemsEntityCommand
    ) {
        public OrdersEntity {
            Objects.requireNonNull(id);
            Objects.requireNonNull(memberCommand);
            Objects.requireNonNull(totalAmount);
            Objects.requireNonNull(address);
            Objects.requireNonNull(ordersItemsEntityCommand);
        }
    }
}
