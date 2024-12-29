package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrdersReadWrite {

    /**
     * READ
     * */
    Optional<Orders> findSpecificOrders(Long id);

    List<Tuple> customFindOrders(Long userId, Pageable pageable);

    /**
     * WRITE
     * */
    Orders saveOrders(Orders orders);
}
