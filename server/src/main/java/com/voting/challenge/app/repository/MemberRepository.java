package com.voting.challenge.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.voting.challenge.domain.Member;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String name);

    default Member findOneByEmail(String username) {
        return findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
