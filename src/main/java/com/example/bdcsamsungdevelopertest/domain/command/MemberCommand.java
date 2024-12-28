package com.example.bdcsamsungdevelopertest.domain.command;

import org.springframework.data.domain.Pageable;

import java.util.Objects;

public class MemberCommand {

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

    public record ValidatedRegister(
        String name,
        String email,
        String address
    ) {
        public ValidatedRegister {
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }

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

    // TODO deprecated
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

    public record SearchList(Pageable pageable) {}
}