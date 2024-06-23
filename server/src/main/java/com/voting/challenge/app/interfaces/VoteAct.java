package com.voting.challenge.app.interfaces;

import com.voting.challenge.domain.payload.VoteRequest;

public interface VoteAct {
    void vote(final VoteRequest voteRequest);
}
