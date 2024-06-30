package com.voting.challenge.domain.payload;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SessionsByMember(@NotNull Long sessionsOpenedCount,
                               @NotNull Long sessionsVotedCount) {
}
