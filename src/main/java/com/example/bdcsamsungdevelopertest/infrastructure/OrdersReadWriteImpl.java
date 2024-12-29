package com.example.bdcsamsungdevelopertest.infrastructure;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.example.bdcsamsungdevelopertest.domain.interfaces.OrdersReadWrite;
import com.example.bdcsamsungdevelopertest.infrastructure.jpa.OrdersRepository;
import com.example.bdcsamsungdevelopertest.infrastructure.querydsl.OrdersQueryRepository;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrdersReadWriteImpl implements OrdersReadWrite {

    private final OrdersRepository ordersRepository;
    private final OrdersQueryRepository ordersQueryRepository;

    public OrdersReadWriteImpl(
        OrdersRepository ordersRepository,
        OrdersQueryRepository ordersQueryRepository
    ) {
        this.ordersRepository = ordersRepository;
        this.ordersQueryRepository = ordersQueryRepository;
    }

    @Override
    public Optional<Orders> findSpecificOrders(Long id) {
        return ordersRepository.findById(id);
    }

    @Override
    public List<Tuple> customFindOrders(Long userId, Pageable pageable) {
        return ordersQueryRepository.findOrders(userId, pageable);
    }

    @Override
    public Orders saveOrders(Orders orders) {
        return ordersRepository.save(orders);
    }
}
