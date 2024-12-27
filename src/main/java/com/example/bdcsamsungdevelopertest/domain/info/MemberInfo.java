package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class MemberInfo {

    public record RegisterMember(
        String name,
        String email,
        String address
    ) {
        public RegisterMember {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}