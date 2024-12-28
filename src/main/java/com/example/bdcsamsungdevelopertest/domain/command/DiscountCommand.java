package com.example.bdcsamsungdevelopertest.domain.command;

import java.util.Objects;

public class DiscountCommand {

    public record DiscountEntity(
        Long id,
        Integer discountValue,
        Long productId
    ) {
        public DiscountEntity {
            Objects.requireNonNull(id);
            Objects.requireNonNull(discountValue);
            Objects.requireNonNull(productId);
        }
    }
}
