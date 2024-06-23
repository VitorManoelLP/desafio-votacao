package com.voting.challenge.domain;

import com.voting.challenge.domain.payload.CreateUserRequest;
import com.voting.challenge.infra.configuration.WebSecurityConfig;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Entity
@Table(name = "member", schema = "voting", indexes = {
        @Index(name = "idx_member_cpf", columnList = "cpf")
})
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private final UUID id = UUID.randomUUID();

    @NotEmpty
    private String name;

    @CPF
    @NotEmpty
    private String cpf;

    @NotEmpty
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public static Member of(CreateUserRequest request) {
        return new Member(
                request.name(),
                request.cpf().value(),
                WebSecurityConfig.password().encode(request.password())
        );
    }

    @Override
    public String getUsername() {
        return cpf;
    }

}
