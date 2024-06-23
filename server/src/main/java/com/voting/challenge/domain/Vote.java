package com.voting.challenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.voting.challenge.enums.VoteOption;
import com.voting.challenge.enums.converters.VoteOptionConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@ToString(exclude = "session")
@Entity
@Table(name = "vote", schema = "voting")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "id_session", nullable = false)
    private VotingSession session;

    @ManyToOne
    @JsonIgnoreProperties("votes")
    @JoinColumn(name = "voted_by", nullable = false)
    private Member votedBy;

    @NotNull
    @Convert(converter = VoteOptionConverter.class)
    private VoteOption vote;

}
