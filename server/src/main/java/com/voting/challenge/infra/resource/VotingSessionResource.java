package com.voting.challenge.infra.resource;

import java.util.Optional;

import com.voting.challenge.app.interfaces.InitializeSession;
import com.voting.challenge.app.interfaces.ReportVote;
import com.voting.challenge.app.interfaces.SessionView;
import com.voting.challenge.app.interfaces.VoteAct;
import com.voting.challenge.domain.payload.*;
import com.voting.challenge.enums.SessionSearchType;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/{type}")
    public ResponseEntity<Page<VotingSessionInfo>> getSessions(@PathVariable("type") SessionSearchType sessionSearchType,
            @RequestParam(value = "search", defaultValue = "") String search,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(sessionView.getSessions(sessionSearchType, search, pageable));
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
    public ResponseEntity<SessionsByMember> getSessionInfoCount() {
        return ResponseEntity.ok(sessionView.byMember());
    }

}
