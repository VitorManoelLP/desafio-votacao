package com.voting.challenge.app.interfaces;

import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;

public interface SessionView {
    VotingSessionInfo view(String code);
    SessionsByMember byMember();
}
