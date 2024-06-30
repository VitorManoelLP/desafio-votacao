package com.voting.challenge.app.service.search.types;

import com.voting.challenge.app.util.SecurityUtil;
import com.voting.challenge.domain.Topic;
import com.voting.challenge.domain.VotingSession;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class SessionSpecificationCreated implements SessionSpecificationFactory {

    @Override
    public Predicate onFilter(Root<VotingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Join<VotingSession, Topic> topic = root.join("topic");
        return criteriaBuilder.equal(topic.get("owner").get("id"), SecurityUtil.getIdUser());
    }

}
