package com.voting.challenge.domain.payload;

import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record RegisterSessionDTO(@NotNull String description, Optional<Expiration> expiration) {
}
