package com.voting.challenge.app.service;

import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.Vote;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class SessionInfo implements SessionView {

    private final VotingSessionRepository votingSessionRepository;

    @Override
    public VotingSessionInfo view(String code) {
        String username = SecurityUtil.getCPF();
        log.info("Viewing session with code: {} for user: {}", code, username);
        VotingSessionInfo sessionInfo = votingSessionRepository.view(code, username);
        log.info("Session info retrieved: {}", sessionInfo);
        return sessionInfo;
    }

    @Override
    public SessionsByMember byMember() {
        final String cpf = SecurityUtil.getCPF();
        log.info("Retrieving sessions for member with CPF: {}", cpf);
        final List<VotingSession> sessions = votingSessionRepository.findAllSessionsByMember(cpf);
        log.debug("Found sessions: {}", sessions);
        final Map<String, List<VotingSession>> sessionsByOwner = sessions.stream()
                .collect(Collectors.groupingBy(session -> session.getTopic().getOwner().getCpf()));
        log.debug("Grouped sessions by owner: {}", sessionsByOwner);
        final Map<String, List<VotingSession>> sessionsByVoted = getSessionsGroupedByVoted(sessions);
        log.debug("Grouped sessions by voted: {}", sessionsByVoted);
        final Map<VotingSession, String> voteBySession = getCPFGroupedBySessions(sessions, cpf);
        log.debug("Mapped votes by session: {}", voteBySession);
        SessionsByMember sessionsByMember = buildSessionMember(sessionsByOwner, cpf, sessionsByVoted, voteBySession);
        log.info("Sessions by member built: {}", sessionsByMember);
        return sessionsByMember;
    }

    private Map<VotingSession, String> getCPFGroupedBySessions(List<VotingSession> sessions, String cpf) {
        return sessions.stream()
                .collect(Collectors.toMap(Function.identity(), session -> session.getVotes().stream()
                        .filter(vote -> vote.getVotedBy().getCpf().equals(cpf))
                        .map(vote -> vote.getVote().getCaption())
                        .findFirst()
                        .orElse("")));
    }

    private Map<String, List<VotingSession>> getSessionsGroupedByVoted(List<VotingSession> sessions) {
        return sessions.stream()
                .collect(Collectors.groupingBy(session -> session.getVotes().stream()
                        .map(Vote::getVotedBy)
                        .map(Member::getCpf)
                        .findFirst()
                        .orElse("")));
    }

    private SessionsByMember buildSessionMember(final Map<String, List<VotingSession>> sessionsByOwner,
                                                final String cpf,
                                                final Map<String, List<VotingSession>> sessionsByVoted,
                                                final Map<VotingSession, String> voteBySession) {
        log.debug("Building session member data");
        return new SessionsByMember(
                (long) sessionsByOwner.getOrDefault(cpf, new ArrayList<>()).size(),
                (long) sessionsByVoted.getOrDefault(cpf, new ArrayList<>()).size(),
                sessionsByOwner.getOrDefault(cpf, new ArrayList<>())
                        .stream()
                        .map(c -> createInfo(c, false, ""))
                        .toList(),
                sessionsByVoted.getOrDefault(cpf, new ArrayList<>())
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
