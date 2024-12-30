package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.Objects;

public class DiscountRequestDto {

    public record DiscountCreate(
        Long productId,
        Integer discountValue
    ) {
        public DiscountCreate {
            Objects.requireNonNull(productId);
            Objects.requireNonNull(discountValue);
        }
    }

    public record DiscountUpdate(
        Integer discountValue
    ) {
        public DiscountUpdate {
            Objects.requireNonNull(discountValue);
        }
    }
}
