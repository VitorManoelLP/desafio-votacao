package com.voting.challenge.app.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voting.challenge.app.interfaces.MarkConsultedSession;
import com.voting.challenge.app.repository.LastConsultedSessionRepository;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.LastConsultedSessions;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.VotingSession;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MarkLastConsultedSessions implements MarkConsultedSession {

    private final LastConsultedSessionRepository lastConsultedSessionRepository;
    private final EntityManager entityManager;

    @Override
    public void mark(UUID sessionId) {
        final Optional<LastConsultedSessions> lastExistingConsult = lastConsultedSessionRepository.findLastConsultByLoggedUser();
        lastExistingConsult.ifPresentOrElse(existing -> editExistingConsult(sessionId, existing),
                () -> createNewConsult(sessionId));
    }

    @Override
    public Optional<LastConsultedSessions> get() {
        return lastConsultedSessionRepository.findLastConsultByLoggedUser();
    }

    private void editExistingConsult(UUID sessionId, LastConsultedSessions existing) {
        VotingSession votingSession = entityManager.getReference(VotingSession.class, sessionId);
        existing.setVotingSession(votingSession);
        existing.setConsultHour(LocalDateTime.now());
    }

    private void createNewConsult(UUID sessionId) {
        Member viewer = entityManager.getReference(Member.class, SecurityUtil.getIdUser());
        VotingSession votingSession = entityManager.getReference(VotingSession.class, sessionId);
        lastConsultedSessionRepository.save(new LastConsultedSessions(
                votingSession,
                viewer,
                LocalDateTime.now()
        ));
    }

}
