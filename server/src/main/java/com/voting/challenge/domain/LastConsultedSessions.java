package com.voting.challenge.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "last_consulted_sessions", schema = "voting")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class LastConsultedSessions {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name = "id_session", nullable = false)
    private VotingSession votingSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_viewer", nullable = false)
    private Member viewer;

    @Setter
    @Column(nullable = false, name = "consult_hour")
    private LocalDateTime consultHour;

}
