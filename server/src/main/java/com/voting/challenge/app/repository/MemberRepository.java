package com.voting.challenge.app.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voting.challenge.domain.Member;

import jakarta.persistence.EntityNotFoundException;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    default Member findOneById(String id) {
        return findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
