package com.voting.challenge.app.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.voting.challenge.domain.payload.LastConsult;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;
import com.voting.challenge.enums.SessionSearchType;

public interface SessionView {
    VotingSessionInfo view(String code);
    SessionsByMember byMember();
    Optional<LastConsult> getLastConsult();
    Page<VotingSessionInfo> getSessions(SessionSearchType sessionSearchType, String search, Pageable pageable);
}
