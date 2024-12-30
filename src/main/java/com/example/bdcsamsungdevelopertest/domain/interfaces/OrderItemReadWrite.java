package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.command.OrdersProductRequestCommand;
import com.example.bdcsamsungdevelopertest.domain.entity.OrderItem;
import com.querydsl.core.Tuple;

import java.util.List;

public interface OrderItemReadWrite {

    /**
     * READ
     * */
    List<Tuple> customFindOrderItemGroup(OrdersProductRequestCommand searchCommand);

    /**
     * WRITE
     * */
    OrderItem saveOrderItem(OrderItem orderItem);
}
