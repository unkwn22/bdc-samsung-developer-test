package com.example.bdcsamsungdevelopertest.interfaces.dto;

import java.util.Objects;

public class MemberDto {

    public record Register(
        String name,
        String email,
        String address
    ) {
        public Register {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}