package com.voting.challenge.domain.payload;

public record CPF(String value) {
    @Override
    public String value() {
        return value.replace(".", "").replace("-", "");
    }
}
