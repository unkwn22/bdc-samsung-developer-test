package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrdersReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.OrdersRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrdersReadWriteImpl implements OrdersReadWrite {

    private final OrdersRepository ordersRepository;

    public OrdersReadWriteImpl(
        OrdersRepository ordersRepository
    ) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Orders saveOrders(Orders orders) {
        return ordersRepository.save(orders);
    }
}
