package com.voting.challenge.domain.payload;

import com.voting.challenge.enums.VoteOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record VotingSessionInfo(@NotBlank String topic,
                                @NotBlank String code,
                                @NotBlank String isOpen,
                                @NotNull boolean alreadyVote,
                                @NotNull String yourVote,
                                @NotNull LocalDateTime openedAt,
                                @NotNull LocalDateTime closeAt) {

    public VotingSessionInfo(@NotBlank String topic,
                             @NotBlank String code,
                             @NotBlank String isOpen,
                             @NotNull boolean alreadyVote,
                             @NotNull VoteOption yourVote,
                             @NotNull LocalDateTime openedAt,
                             @NotNull LocalDateTime closeAt) {
        this(topic, code, isOpen, alreadyVote, yourVote.getCaption(), openedAt, closeAt);
    }
}
