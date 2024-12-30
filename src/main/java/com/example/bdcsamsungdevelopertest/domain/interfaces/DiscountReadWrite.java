package com.example.bdcsamsungdevelopertest.domain.interfaces;

import com.example.bdcsamsungdevelopertest.domain.entity.Discount;
import com.example.bdcsamsungdevelopertest.domain.entity.Product;

import java.util.Optional;

public interface DiscountReadWrite {
    /**
     * READ
     * */
    Optional<Discount> findSpecificDiscount(Product product);

    /**
     * WRITE
     * */
    Discount saveDiscount(Discount discount);
}
