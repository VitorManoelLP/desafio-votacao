package com.voting.challenge.domain.payload;

import com.voting.challenge.enums.VoteOption;
import jakarta.validation.constraints.NotNull;

public record VoteRequest(@NotNull String session, @NotNull VoteOption voteOption) {
}
