package com.voting.challenge.infra.resource;

import java.util.Optional;

import com.voting.challenge.app.interfaces.InitializeSession;
import com.voting.challenge.app.interfaces.ReportVote;
import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.interfaces.VoteAct;
import com.voting.challenge.app.repository.LastConsultedSessionRepository;
import com.voting.challenge.domain.payload.*;
import com.voting.challenge.infra.configuration.WebSecurityConfig;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.startup.WebAnnotationSet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/session")
@SecurityRequirement(name = "Authorization")
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

    @GetMapping("/last-consulted")
    public ResponseEntity<Optional<LastConsult>> getLastConsulted() {
        return ResponseEntity.ok(sessionView.getLastConsult());
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
