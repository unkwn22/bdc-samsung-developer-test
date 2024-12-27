package com.example.bdcsamsungdevelopertest.domain.info;

import java.util.Objects;

public class MemberInfo {

    public record MemberEntityInfo(
        String name,
        String email,
        String address
    ) {
        public MemberEntityInfo {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}