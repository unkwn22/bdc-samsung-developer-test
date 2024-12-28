package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class DiscountInfo {

    public record DiscountEntity(
        Long productId,
        Integer discountValue
    ) {
        public DiscountEntity {
            Objects.requireNonNull(productId);
            Objects.requireNonNull(discountValue);
        }
    }
}
