package com.voting.challenge.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.voting.challenge.app.interfaces.MarkConsultedSession;
import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.repository.LastConsultedSessionRepository;
import com.voting.challenge.app.repository.MemberRepository;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.LastConsultedSessions;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.Vote;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.LastConsult;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;

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
        log.info("Session info retrieved: {}", sessionInfo);
        markConsultedSession.mark(votingSessionRepository.getIdByCode(code));
        return sessionInfo;
    }

    @Override
    public SessionsByMember byMember() {
        final String idUser = SecurityUtil.getIdUser();
        log.info("Retrieving sessions for member with CPF: {}", idUser);
        final List<VotingSession> sessions = votingSessionRepository.findAllSessionsByMember(idUser);
        log.debug("Found sessions: {}", sessions);
        final Map<String, List<VotingSession>> sessionsByOwner = sessions.stream()
                .collect(Collectors.groupingBy(session -> session.getTopic().getOwner().getId()));
        log.debug("Grouped sessions by owner: {}", sessionsByOwner);
        final Map<String, List<VotingSession>> sessionsByVoted = getSessionsGroupedByVoted(sessions);
        log.debug("Grouped sessions by voted: {}", sessionsByVoted);
        final Map<VotingSession, String> voteBySession = getCPFGroupedBySessions(sessions, idUser);
        log.debug("Mapped votes by session: {}", voteBySession);
        SessionsByMember sessionsByMember = buildSessionMember(sessionsByOwner, idUser, sessionsByVoted, voteBySession);
        log.info("Sessions by member built: {}", sessionsByMember);
        return sessionsByMember;
    }

    @Override
    public Optional<LastConsult> getLastConsult() {
        final Optional<LastConsultedSessions> lastConsult = markConsultedSession.get();
        return lastConsult.map(consult -> {
            final VotingSessionInfo view = votingSessionRepository.view(consult.getVotingSession().getCode(), consult.getViewer().getId());
            return new LastConsult(view, consult.getConsultHour());
        });
    }

    private Map<VotingSession, String> getCPFGroupedBySessions(List<VotingSession> sessions, String idUser) {
        return sessions.stream()
                .collect(Collectors.toMap(Function.identity(), session -> session.getVotes().stream()
                        .filter(vote -> vote.getVotedBy().getId().equals(idUser))
                        .map(vote -> vote.getVote().getCaption())
                        .findFirst()
                        .orElse("")));
    }

    private Map<String, List<VotingSession>> getSessionsGroupedByVoted(List<VotingSession> sessions) {
        return sessions.stream()
                .collect(Collectors.groupingBy(session -> session.getVotes().stream()
                        .map(Vote::getVotedBy)
                        .map(Member::getId)
                        .findFirst()
                        .orElse("")));
    }

    private SessionsByMember buildSessionMember(final Map<String, List<VotingSession>> sessionsByOwner,
            final String idUser,
            final Map<String, List<VotingSession>> sessionsByVoted,
            final Map<VotingSession, String> voteBySession) {
        log.debug("Building session member data");
        return new SessionsByMember(
                (long) sessionsByOwner.getOrDefault(idUser, new ArrayList<>()).size(),
                (long) sessionsByVoted.getOrDefault(idUser, new ArrayList<>()).size(),
                sessionsByOwner.getOrDefault(idUser, new ArrayList<>())
                        .stream()
                        .map(c -> createInfo(c, false, ""))
                        .toList(),
                sessionsByVoted.getOrDefault(idUser, new ArrayList<>())
                        .stream()
                        .map(c -> createInfo(c, true, voteBySession.getOrDefault(c, "")))
                        .toList()
        );
    }

    private VotingSessionInfo createInfo(VotingSession session,
            boolean alreadyVote,
            String yourVote) {
        log.debug("Creating session info for session: {}", session);
        VotingSessionInfo sessionInfo = new VotingSessionInfo(
                session.getTopic().getDescription(),
                session.getCode(),
                session.isOpen() ? "Sim" : "Não",
                alreadyVote,
                yourVote,
                session.getStartTime(),
                session.getEndTime()
        );
        log.debug("Created session info: {}", sessionInfo);
        return sessionInfo;
    }

}
