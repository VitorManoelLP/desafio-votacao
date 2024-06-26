package com.voting.challenge.domain;

import com.voting.challenge.infra.configuration.WebSecurityConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@ToString
@Entity
@Immutable
@Table(name = "user_entity", schema = "keycloak")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    private String id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String email;

}
