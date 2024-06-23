package com.voting.challenge.domain.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(CPF cpf, @NotEmpty String password) {

    @JsonCreator
    public LoginRequest(@org.hibernate.validator.constraints.br.CPF String cpf, String password) {
        this(new CPF(cpf), password);
    }

}
