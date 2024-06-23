package com.voting.challenge.domain.payload;

import com.voting.challenge.enums.ExpirationType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record Expiration(@NotNull Long value, @NotNull ExpirationType expirationType) {
    public LocalDateTime applyExpiration(LocalDateTime date) {
        return expirationType.getApplyExpirationOnDate().apply(value, date);
    }
}
