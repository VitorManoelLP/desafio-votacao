package com.voting.challenge.domain.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(@NotBlank String name, @NotBlank String password, CPF cpf) {

    @JsonCreator
    public CreateUserRequest(@NotBlank String name, @NotBlank String password, @org.hibernate.validator.constraints.br.CPF String cpf) {
        this(name, password, new CPF(cpf));
    }

}
