package com.voting.challenge.app.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import com.voting.challenge.domain.LastConsultedSessions;

@Repository
public interface LastConsultedSessionRepository extends JpaRepository<LastConsultedSessions, UUID> {

    Optional<LastConsultedSessions> findByViewerId(String id);

    default Optional<LastConsultedSessions> findLastConsultByLoggedUser() {
        return findByViewerId(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
