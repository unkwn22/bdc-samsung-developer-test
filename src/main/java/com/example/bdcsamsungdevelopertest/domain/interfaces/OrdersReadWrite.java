package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;

public interface OrdersReadWrite {

    /**
     * WRITE
     * */
    Orders saveOrders(Orders orders);
}
