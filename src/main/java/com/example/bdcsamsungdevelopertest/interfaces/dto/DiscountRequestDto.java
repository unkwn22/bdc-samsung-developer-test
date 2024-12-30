package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.Objects;

public record DiscountRequestDto(
    Long productId,
    Integer discountValue
) {
    public DiscountRequestDto {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(discountValue);
    }
}
