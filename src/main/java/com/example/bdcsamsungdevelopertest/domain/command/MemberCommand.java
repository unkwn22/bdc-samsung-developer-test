package com.example.bdcsamsungdevelopertest.domain.command;

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

    public record MemberEntityCommand(
        Long id,
        String name,
        String email,
        String address
    ) {
        public MemberEntityCommand {
            Objects.requireNonNull(id);
            Objects.requireNonNull(name);
            Objects.requireNonNull(email);
            Objects.requireNonNull(address);
        }
    }
}