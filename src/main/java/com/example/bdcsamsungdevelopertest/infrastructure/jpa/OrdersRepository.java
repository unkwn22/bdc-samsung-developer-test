package com.example.bdcsamsungdevelopertest.infrastructure.jpa;

import com.example.bdcsamsungdevelopertest.domain.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
