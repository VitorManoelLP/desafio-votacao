package com.voting.challenge.app.service;

import com.voting.challenge.domain.Vote;
import com.voting.challenge.domain.payload.VoteRequest;
import com.voting.challenge.enums.VoteOption;
import com.voting.challenge.exception.MemberAlreadyVotedException;
import com.voting.challenge.exception.SessionClosedException;
import com.voting.challenge.exception.SessionNotFoundException;
import com.voting.challenge.extension.TestContainerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

@Sql(statements = {
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('f3e15192-e428-4352-a240-bafa22eebd1e', 'foo', 'foo@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('cc4e1acc-830d-439d-9075-3323fa667cec', 'foo citizen', 'foocitizen@gmail.com')",
        "INSERT INTO voting.topic(id, description, id_owner) VALUES('338b005a-18bc-40b0-9fb5-7cf00edf4d3a', 'Yes or No?', 'f3e15192-e428-4352-a240-bafa22eebd1e')",
        """
        INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code)
        VALUES('cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-3333');
        """,
        """
        INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code)
        VALUES('45bedd4a-3c04-48e4-bc8b-bbdd779c03bd', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2024-06-19 14:30:00', '1111-2222-5555');
        """
})
@WithMockUser(username = "foocitizen@gmail.com")
public class VoteActionTest extends TestContainerExtension {

    @Autowired
    private VoteAction voteAction;

    @Test
    public void simpleVote() {
        voteAction.vote(new VoteRequest("1111-2222-3333", VoteOption.NAO));
        final Vote vote = getEm().createQuery("SELECT vote FROM Vote vote WHERE vote.votedBy.id = :id", Vote.class)
                .setParameter("id", "cc4e1acc-830d-439d-9075-3323fa667cec")
                .getSingleResult();

        Assertions.assertThat(vote.getVotedBy().getId()).isEqualTo("cc4e1acc-830d-439d-9075-3323fa667cec");
        Assertions.assertThat(vote.getVote()).isEqualTo(VoteOption.NAO);
        Assertions.assertThat(vote.getSession().getCode()).isEqualTo("1111-2222-3333");
    }

    @Test
    public void shouldThrowIfAlreadyHasVoteBy() {
        voteAction.vote(new VoteRequest("1111-2222-3333", VoteOption.SIM));
        Assertions.assertThatThrownBy(() -> voteAction.vote(new VoteRequest("1111-2222-3333", VoteOption.SIM)))
                .isInstanceOf(MemberAlreadyVotedException.class)
                .hasMessage("You already voted in this topic.");
    }

    @Test
    public void shouldThrowIfSessionDoesNotExists() {
        Assertions.assertThatThrownBy(() -> voteAction.vote(new VoteRequest("1111-2222-4444", VoteOption.SIM)))
                .isInstanceOf(SessionNotFoundException.class)
                .hasMessage("Session not found for code [1111-2222-4444]");
    }

    @Test
    public void shouldThrowIfSessionAlreadyClosed() {
        Assertions.assertThatThrownBy(() -> voteAction.vote(new VoteRequest("1111-2222-5555", VoteOption.SIM)))
                .isInstanceOf(SessionClosedException.class)
                .hasMessage("Session of votes already closed");
    }

}