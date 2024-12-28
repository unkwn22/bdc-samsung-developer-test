package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.Objects;

public record MemberRequestDto(
    String name,
    String email,
    String address
) {
    public MemberRequestDto {
        Objects.requireNonNull(name);
        Objects.requireNonNull(email);
        Objects.requireNonNull(address);
    }
}