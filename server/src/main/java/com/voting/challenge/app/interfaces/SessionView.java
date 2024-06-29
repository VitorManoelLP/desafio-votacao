package com.voting.challenge.app.interfaces;

import java.util.Optional;

import com.voting.challenge.domain.payload.LastConsult;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;

public interface SessionView {
    VotingSessionInfo view(String code);
    SessionsByMember byMember();
    Optional<LastConsult> getLastConsult();
}
