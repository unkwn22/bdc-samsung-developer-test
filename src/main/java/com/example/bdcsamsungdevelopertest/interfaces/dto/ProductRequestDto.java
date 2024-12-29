package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.Objects;

public record ProductRequestDto(
    String name,
    Integer price
) {
    public ProductRequestDto {
        Objects.requireNonNull(name);
        Objects.requireNonNull(price);
    }
}
