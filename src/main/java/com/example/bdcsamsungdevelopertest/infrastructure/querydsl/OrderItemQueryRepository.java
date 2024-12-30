package com.example.bdcsamsungdevelopertest.infrastructure.querydsl;

import com.example.bdcsamsungdevelopertest.domain.command.OrdersProductRequestCommand;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemQueryRepository {

    List<Tuple> findOrderItemGroup(OrdersProductRequestCommand searchCommand);
}
