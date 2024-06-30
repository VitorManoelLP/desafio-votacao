package com.voting.challenge.app.service.search.types;

import org.apache.commons.lang3.NotImplementedException;

import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.enums.SessionSearchType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface SessionSpecificationFactory {

    static SessionSpecificationFactory factory(SessionSearchType type) {
        if (SessionSearchType.CREATED.equals(type)) {
            return new SessionSpecificationCreated();
        }
        if (SessionSearchType.VOTED.equals(type)) {
            return new SessionSpecificationVoted();
        }
        throw new NotImplementedException("Session factory not implemented");
    }

    Predicate onFilter(Root<VotingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

}
