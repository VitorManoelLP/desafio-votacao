package com.voting.challenge.app.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voting.challenge.app.interfaces.MarkConsultedSession;
import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.app.service.search.SessionSpecificationSearch;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.LastConsultedSessions;
import com.voting.challenge.domain.payload.LastConsult;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;
import com.voting.challenge.enums.SessionSearchType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SessionInfo implements SessionView {

    private final VotingSessionRepository votingSessionRepository;
    private final MarkConsultedSession markConsultedSession;

    @Override
    public VotingSessionInfo view(String code) {
        final String idUser = SecurityUtil.getIdUser();
        log.info("Viewing session with code: {} for user: {}", code, idUser);
        VotingSessionInfo sessionInfo = votingSessionRepository.view(code, idUser);
        if (Objects.isNull(sessionInfo)) {
            throw new IllegalArgumentException("Você não pode entrar em uma sessão criada por você mesmo.");
        }
        log.info("Session info retrieved: {}", sessionInfo);
        markConsultedSession.mark(votingSessionRepository.getIdByCode(code));
        return sessionInfo;
    }

    @Override
    public SessionsByMember byMember() {
        final String idUser = SecurityUtil.getIdUser();
        log.info("Retrieving sessions for member with ID: {}", idUser);
        return new SessionsByMember(
                votingSessionRepository.countSessionsCreated(idUser),
                votingSessionRepository.countSessionsVoted(idUser)
        );
    }

    @Override
    public Optional<LastConsult> getLastConsult() {
        final Optional<LastConsultedSessions> lastConsult = markConsultedSession.get();
        return lastConsult.map(consult -> {
            final VotingSessionInfo view = votingSessionRepository.view(consult.getVotingSession().getCode(), SecurityUtil.getIdUser());
            return new LastConsult(view, consult.getConsultHour());
        });
    }

    @Override
    public Page<VotingSessionInfo> getSessions(SessionSearchType sessionSearchType, String search, Pageable pageable) {
        return votingSessionRepository.findAll(new SessionSpecificationSearch(search, sessionSearchType), pageable)
                .map(VotingSessionInfo::of);
    }

}
