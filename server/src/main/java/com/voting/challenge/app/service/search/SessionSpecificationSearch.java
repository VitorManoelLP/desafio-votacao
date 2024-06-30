package com.voting.challenge.app.service.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.voting.challenge.app.service.search.types.SessionSpecificationFactory;
import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.enums.SessionSearchType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SessionSpecificationSearch implements Specification<VotingSession> {

    private final String search;
    private final SessionSearchType sessionSearchType;

    @Override
    public Predicate toPredicate(Root<VotingSession> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        final SessionSpecificationFactory factory = SessionSpecificationFactory.factory(sessionSearchType);
        final List<Predicate> filters = new ArrayList<>();
        filters.add(factory.onFilter(root, query, criteriaBuilder));
        if (StringUtils.isNotEmpty(search)) {
            Join<Object, Object> topic = root.join("topic");
            final Predicate searchTopic = criteriaBuilder.like(criteriaBuilder.lower(topic.get("description")), "%".concat(search.toLowerCase().concat("%")));
            filters.add(searchTopic);
        }
        return criteriaBuilder.and(filters.toArray(new Predicate[0]));
    }

}
