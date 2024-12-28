package com.example.bdcsamsungdevelopertest.domain.command;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class MemberCommand {

    public record MemberEntity(
        Long id,
        String name,
        String email,
        String address
    ) {
        public MemberEntity {
            Objects.requireNonNull(id);
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }

    public record SearchList(Pageable pageable) {}
}