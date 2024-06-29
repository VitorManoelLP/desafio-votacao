package com.voting.challenge.app.service;

import com.voting.challenge.app.interfaces.ReportVote;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.payload.CountReport;
import com.voting.challenge.exception.PermissionDeniedVotingSessionView;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VotingReport implements ReportVote {

    private final VotingSessionRepository votingSessionRepository;

    public CountReport count(@NotBlank String sessionCode) {
        verifyPermissionToVoteBy(sessionCode);
        return votingSessionRepository.count(sessionCode);
    }

    private void verifyPermissionToVoteBy(String sessionCode) {
        if (votingSessionRepository.hasNotPermissionToCount(sessionCode, SecurityUtil.getIdUser())) {
            throw new PermissionDeniedVotingSessionView("You don't have permission to count votes if you didn't vote");
        }
    }

}
