package com.voting.challenge.domain.payload;

import java.time.LocalDateTime;

public record LastConsult(VotingSessionInfo sessionInfo, LocalDateTime consultedAt) {
}
