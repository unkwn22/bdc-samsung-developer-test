package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class MemberInfo {

    public record MemberEntity(
        String name,
        String email,
        String address
    ) {
        public MemberEntity {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}