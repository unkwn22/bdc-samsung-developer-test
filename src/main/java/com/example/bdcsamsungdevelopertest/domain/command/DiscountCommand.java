package com.example.bdcsamsungdevelopertest.domain.command;

import java.util.Objects;

public class DiscountCommand {

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
