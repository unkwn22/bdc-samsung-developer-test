package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.command.OrdersProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrderItemReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.OrderItemRepository;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.OrderItemQueryRepository;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemReadWriteImpl implements OrderItemReadWrite {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemQueryRepository orderItemQueryRepository;

    public OrderItemReadWriteImpl(
        OrderItemRepository orderItemRepository,
        OrderItemQueryRepository orderItemQueryRepository
    ) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemQueryRepository = orderItemQueryRepository;
    }

    @Override
    public List<Tuple> customFindOrderItemGroup(OrdersProductRequestCommand searchCommand) {
        return orderItemQueryRepository.findOrderItemGroup(searchCommand);
    }

    @Override
    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
