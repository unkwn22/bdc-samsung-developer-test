package com.example.bdcsamsungdevelopertest.infrastructure.querydsl;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersQueryRepository {

    List<Tuple> findOrders(Long userId, Pageable pageable);
}
