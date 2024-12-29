package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;

public interface OrderItemReadWrite {

    /**
     * WRITE
     * */
    OrderItem saveOrderItem(OrderItem orderItem);
}
