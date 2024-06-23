package com.voting.challenge.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.UUID;

@Getter
@ToString
@Entity
@Table(name = "topic", schema = "voting")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id = UUID.randomUUID();

    @NotEmpty
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_owner", nullable = false)
    private Member owner;

}
