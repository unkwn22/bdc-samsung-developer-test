package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrderItemReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.OrderItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemReadWriteImpl implements OrderItemReadWrite {

    private final OrderItemRepository orderItemRepository;

    public OrderItemReadWriteImpl(
        OrderItemRepository orderItemRepository
    ) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
