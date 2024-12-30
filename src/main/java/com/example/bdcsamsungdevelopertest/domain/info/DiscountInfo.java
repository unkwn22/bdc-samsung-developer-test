package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class DiscountInfo {

    public record DiscountEntity(
        Long id,
        Integer discountValue
    ) {
        public DiscountEntity {
            Objects.requireNonNull(id);
            Objects.requireNonNull(discountValue);
        }
    }
}
