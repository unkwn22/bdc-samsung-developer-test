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

    public record Search(
        String name,
        String email,
        String address
    ) {
        public Search {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }

    public record Update(
        String name,
        String email,
        String address
    ) {
        public Update {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}