package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;

import java.util.Optional;

public interface OrdersReadWrite {

    /**
     * READ
     * */
    Optional<Orders> findSpecificOrders(Long id);

    /**
     * WRITE
     * */
    Orders saveOrders(Orders orders);
}
