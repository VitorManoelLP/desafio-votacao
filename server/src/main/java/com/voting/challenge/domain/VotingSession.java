package com.voting.challenge.domain;

import com.voting.challenge.app.util.CodeGenerator;
import com.voting.challenge.domain.payload.Expiration;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Entity
@Table(name = "voting_session", schema = "voting", indexes = {
        @Index(name = "idx_voting_session_code", columnList = "code")
})
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id = UUID.randomUUID();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_topic", nullable = false)
    private Topic topic;

    @Column(name = "start_time")
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Formula("(NOW() <= end_time)")
    private boolean isOpen = Boolean.TRUE;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_session")
    private final List<Vote> votes = new ArrayList<>();

    @NotEmpty
    private String code;

    public boolean hasAnyVotesBy(@NonNull Member member) {
        return getVotes().stream().anyMatch(vote -> vote.getVotedBy().equals(member));
    }

    public static VotingSession of(Topic topic, Expiration expiration) {
        final LocalDateTime now = LocalDateTime.now();
        return new VotingSession(topic, now, expiration.applyExpiration(now), true, CodeGenerator.generateKey());
    }

}
