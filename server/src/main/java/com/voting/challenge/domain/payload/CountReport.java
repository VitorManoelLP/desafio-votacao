package com.voting.challenge.domain.payload;

import jakarta.validation.constraints.NotNull;

public record CountReport(
        @NotNull String description,
        @NotNull boolean isOpen,
        @NotNull String percentYes,
        @NotNull String percentNo,
        @NotNull Long countYes,
        @NotNull Long countNo,
        @NotNull Long totalVotes) {
}
