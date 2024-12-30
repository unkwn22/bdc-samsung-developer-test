package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;

import java.util.Optional;

public interface DiscountReadWrite {
    /**
     * READ
     * */
    Optional<Discount> findSpecificDiscount(Product product);

    Optional<Discount> findSpecificDiscount(Long id);

    /**
     * WRITE
     * */
    Discount saveDiscount(Discount discount);

    void deleteDiscount(Discount discount);
}
