package com.voting.challenge.app.interfaces;

import java.util.Optional;
import java.util.UUID;

import com.voting.challenge.domain.LastConsultedSessions;

public interface MarkConsultedSession {
    void mark(UUID sessionId);
    Optional<LastConsultedSessions> get();
}
