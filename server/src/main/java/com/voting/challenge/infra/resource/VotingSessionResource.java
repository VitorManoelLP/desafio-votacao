package com.voting.challenge.infra.resource;

import com.voting.challenge.app.interfaces.InitializeSession;
import com.voting.challenge.app.interfaces.ReportVote;
import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.interfaces.VoteAct;
import com.voting.challenge.domain.payload.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
public class VotingSessionResource {

    private final InitializeSession initializeSession;
    private final VoteAct voteAct;
    private final ReportVote reportVote;
    private final SessionView sessionView;

    @PostMapping("/init")
    public ResponseEntity<RegisterSessionResponse> initializeSession(@RequestBody @Valid RegisterSessionDTO sessionParams) {
        return ResponseEntity.ok(initializeSession.init(sessionParams));
    }

    @PostMapping("/vote/v1")
    public ResponseEntity<Void> vote(@RequestBody @Valid VoteRequest request) {
        voteAct.vote(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count/v1/{sessionCode}")
    public ResponseEntity<CountReport> count(@PathVariable String sessionCode) {
        return ResponseEntity.ok(reportVote.count(sessionCode));
    }

    @GetMapping("/view/v1/{sessionCode}")
    public ResponseEntity<VotingSessionInfo> getInfo(@PathVariable String sessionCode) {
        return ResponseEntity.ok(sessionView.view(sessionCode));
    }

    @GetMapping
    public ResponseEntity<SessionsByMember> getSessions() {
        return ResponseEntity.ok(sessionView.byMember());
    }

}
