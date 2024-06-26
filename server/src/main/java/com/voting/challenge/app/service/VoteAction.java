package com.voting.challenge.app.service;

import com.voting.challenge.app.interfaces.VoteAct;
import com.voting.challenge.app.repository.MemberRepository;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.Vote;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.VoteRequest;
import com.voting.challenge.exception.MemberAlreadyVotedException;
import com.voting.challenge.exception.PermissionDeniedVotingSessionView;
import com.voting.challenge.exception.SessionClosedException;
import com.voting.challenge.exception.SessionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class VoteAction implements VoteAct {

    private final MemberRepository memberRepository;
    private final VotingSessionRepository votingSessionRepository;

    public void vote(final VoteRequest voteRequest) {
        final String username = SecurityUtil.getEmail();
        log.info("Vote request received from user: {}", username);

        final Member loggedUser = memberRepository.findOneByEmail(username);
        log.debug("Logged user details: {}", loggedUser);

        final VotingSession session = getSession(voteRequest, loggedUser);
        log.info("Voting session retrieved: {}", session);

        verifyVotes(session, loggedUser);
        log.info("Vote verification passed for user: {}", username);

        session.getVotes().add(new Vote(session, loggedUser, voteRequest.voteOption()));
        votingSessionRepository.save(session);
        log.info("Vote successfully saved for session: {} by user: {}", session.getCode(), username);
    }

    private void verifyVotes(VotingSession session, Member loggedUser) {
        if (session.hasAnyVotesBy(loggedUser)) {
            log.warn("User {} already voted in session {}", loggedUser.getEmail(), session.getCode());
            throw new MemberAlreadyVotedException("You already voted in this topic.");
        }
        if (!session.isOpen()) {
            log.warn("Voting session {} is closed", session.getCode());
            throw new SessionClosedException("Session of votes already closed");
        }
        if (session.getTopic().getOwner().equals(loggedUser)) {
            log.warn("Owner {} attempted to vote in their own session {}", loggedUser.getEmail(), session.getCode());
            throw new PermissionDeniedVotingSessionView("The owner can't vote in your own voting");
        }
    }

    private VotingSession getSession(VoteRequest voteRequest, Member loggedUser) {
        log.debug("Fetching session for vote request: {}", voteRequest);
        return votingSessionRepository.findByCodeExceptOwnerBy(voteRequest.session(), loggedUser.getId())
                .orElseThrow(() -> {
                    log.error("Session not found for code: {}", voteRequest.session());
                    return new SessionNotFoundException(String.format("Session not found for code [%s]", voteRequest.session()));
                });
    }
}
