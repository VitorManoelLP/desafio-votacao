package com.voting.challenge.app.service.search.types;

import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.VotingSession;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SessionSpecificationVoted implements SessionSpecificationFactory {

    @Override
    public Predicate onFilter(Root<VotingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<Object, Object> votes = root.join("votes");
        Join<Object, Object> votedBy = votes.join("votedBy");
        return criteriaBuilder.equal(votedBy.get("id"), SecurityUtil.getIdUser());
    }

}
