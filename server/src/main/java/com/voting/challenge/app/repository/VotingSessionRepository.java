package com.voting.challenge.app.repository;

import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.CountReport;
import com.voting.challenge.domain.payload.VotingSessionInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, UUID>, JpaSpecificationExecutor<VotingSession> {

    @Query("SELECT session FROM VotingSession session " +
            " JOIN session.topic topic " +
            " WHERE session.code = :code AND topic.owner.id <> :member ")
    Optional<VotingSession> findByCodeExceptOwnerBy(@Param("code") String code, @Param("member") String member);

    @Query("SELECT COUNT(session) FROM VotingSession session " +
            " JOIN session.votes vote " +
            " JOIN vote.votedBy voted " +
            " WHERE voted.id = :idUser")
    Long countSessionsVoted(@Param("idUser") String idUser);

    @Query("SELECT COUNT(session) FROM VotingSession session " +
            " JOIN session.topic topic " +
            " JOIN topic.owner owner " +
            " WHERE owner.id = :idUser")
    Long countSessionsCreated(@Param("idUser") String idUser);

    @Query("SELECT COUNT(session) = 0 FROM VotingSession session " +
            " JOIN session.topic topic " +
            " LEFT JOIN session.votes vote " +
            " LEFT JOIN vote.votedBy votedBy " +
            " JOIN topic.owner owner " +
            " WHERE (owner.id = :member OR votedBy.id = :member) " +
            " AND session.code = :code ")
    boolean hasNotPermissionToCount(@Param("code") String code, @Param("member") String member);

    @Query("SELECT new com.voting.challenge.domain.payload.CountReport(" +
            "   topic.description, " +
            "   session.isOpen, " +
            "   (ROUND(COUNT(CASE WHEN vote.vote = com.voting.challenge.enums.VoteOption.SIM THEN 1 END) * 100.0 / COUNT(vote)) || '%'), " +
            "   (ROUND(COUNT(CASE WHEN vote.vote = com.voting.challenge.enums.VoteOption.NAO THEN 1 END) * 100.0 / COUNT(vote)) || '%'), " +
            "   COUNT(CASE WHEN vote.vote = com.voting.challenge.enums.VoteOption.SIM THEN 1 END), " +
            "   COUNT(CASE WHEN vote.vote = com.voting.challenge.enums.VoteOption.NAO THEN 1 END), " +
            "   COUNT(vote)) " +
            " FROM VotingSession session " +
            " JOIN session.topic topic " +
            " JOIN session.votes vote " +
            " WHERE session.code = :code " +
            " GROUP BY topic.description, session.isOpen ")
    CountReport count(@Param("code") String code);

    @Query("SELECT new com.voting.challenge.domain.payload.VotingSessionInfo(" +
            "   topic.description," +
            "   session.code, " +
            "   session.isOpen," +
            "   voted IS NOT NULL," +
            "   vote.vote, " +
            "   session.startTime, " +
            "   session.endTime " +
            ") FROM VotingSession session " +
            " JOIN session.topic topic " +
            " LEFT JOIN session.votes vote " +
            " LEFT JOIN vote.votedBy voted " +
            " WHERE session.code = :code AND (voted.id = :member OR topic.owner.id <> :member)")
    VotingSessionInfo view(@Param("code") String code, @Param("member") String member);

    @Query("SELECT vo.id FROM VotingSession vo WHERE vo.code = :code")
    UUID getIdByCode(@Param("code") String code);
}
