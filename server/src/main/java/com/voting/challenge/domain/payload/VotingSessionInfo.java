package com.voting.challenge.domain.payload;

import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.enums.VoteOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record VotingSessionInfo(@NotBlank String topic,
                                @NotBlank String code,
                                @NotBlank boolean isOpen,
                                @NotNull boolean alreadyVote,
                                @NotNull String yourVote,
                                @NotNull LocalDateTime openedAt,
                                @NotNull LocalDateTime closeAt) {

    public VotingSessionInfo(@NotBlank String topic,
                             @NotBlank String code,
                             @NotBlank boolean isOpen,
                             @NotNull boolean alreadyVote,
                             @NotNull VoteOption yourVote,
                             @NotNull LocalDateTime openedAt,
                             @NotNull LocalDateTime closeAt) {
        this(topic, code, isOpen, alreadyVote, yourVote.getCaption(), openedAt, closeAt);
    }

    public static VotingSessionInfo of(VotingSession session) {
        return new VotingSessionInfo(
                session.getTopic().getDescription(),
                session.getCode(),
                session.isOpen(),
                session.getVotes().stream().anyMatch(vote -> vote.getVotedBy().getId().equals(SecurityUtil.getIdUser())),
                session.getVotes().stream().filter(vote -> vote.getVotedBy().getId().equals(SecurityUtil.getIdUser()))
                        .findFirst()
                        .map(vote -> vote.getVote().getCaption())
                        .orElse(""),
                session.getStartTime(),
                session.getEndTime());
    }
}
