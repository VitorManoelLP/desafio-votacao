package com.voting.challenge.app.repository;

import com.voting.challenge.domain.Member;
import com.voting.challenge.domain.payload.CreateUserRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByCpf(String name);

    @Query("SELECT member.name FROM Member member WHERE member.cpf = :cpf")
    Optional<String> findNameByCpf(@Param("cpf") String cpf);

    @Query("SELECT COUNT(member) > 0 FROM Member member " +
            " WHERE member.cpf = :cpf")
    boolean alreadyExists(@Param("cpf") String cpf);

    default Member findOneByCpf(String username) {
        return findByCpf(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    default String findOneNameByCpf(String cpf) {
        return findNameByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
