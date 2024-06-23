package com.voting.challenge.app.service;

import com.voting.challenge.app.interfaces.InitializeSession;
import com.voting.challenge.app.repository.MemberRepository;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.Topic;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.Expiration;
import com.voting.challenge.domain.payload.RegisterSessionDTO;
import com.voting.challenge.domain.payload.RegisterSessionResponse;
import com.voting.challenge.enums.ExpirationType;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RegisterTopic implements InitializeSession {

    private final MemberRepository memberRepository;
    private final VotingSessionRepository votingSessionRepository;

    public RegisterSessionResponse init(@NotNull final RegisterSessionDTO registerSession) {
        final String cpf = SecurityContextHolder.getContext().getAuthentication().getName();
        final Member owner = memberRepository.findOneByCpf(cpf);
        final Topic topic = new Topic(registerSession.description(), owner);
        log.debug("Starting topic creating with topic {}. Created by {}", registerSession.description(),cpf);
        final VotingSession votingSession = VotingSession.of(topic, registerSession.expiration()
                .orElse(new Expiration(1L, ExpirationType.MINUTES)));
        votingSessionRepository.save(votingSession);
        log.debug("Session created with code {}", votingSession.getCode());
        return new RegisterSessionResponse(votingSession.getCode());
    }

}
