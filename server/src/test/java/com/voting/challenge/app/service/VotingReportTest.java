package com.voting.challenge.app.service;

import com.voting.challenge.domain.payload.CountReport;
import com.voting.challenge.exception.PermissionDeniedVotingSessionView;
import com.voting.challenge.extension.TestContainerExtension;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

@Sql(statements = {
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('f3e15192-e428-4352-a240-bafa22eebd1e', 'foo', 'foo@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('cc4e1acc-830d-439d-9075-3323fa667cec', 'foo citizen', 'foocitizen@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('d5e3f1a4-9c4d-432f-b2a2-333fae32d8b5', 'bar', 'bar@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('a1b2c3d4-e5f6-7890-ab12-34cd567ef89a', 'baz', 'baz@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('bd000ed7-0509-40d1-92f2-550f2fdb1720', 'outside', 'outside@gmail.com')",
        "INSERT INTO voting.topic(id, description, id_owner) VALUES('338b005a-18bc-40b0-9fb5-7cf00edf4d3a', 'Yes or No?', 'f3e15192-e428-4352-a240-bafa22eebd1e')",
        "INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code) VALUES('cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-3333')",
        "INSERT INTO voting.vote(id, id_session, voted_by, vote) VALUES('1375d202-0c51-4b75-91b9-d777771ccbbb', 'cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', 'f3e15192-e428-4352-a240-bafa22eebd1e', 'Sim')",
        "INSERT INTO voting.vote(id, id_session, voted_by, vote) VALUES('2e71b976-c3a2-4b57-ad21-0450b9bd1e61', 'cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', 'cc4e1acc-830d-439d-9075-3323fa667cec', 'Não')",
        "INSERT INTO voting.vote(id, id_session, voted_by, vote) VALUES('ca3b4850-6abe-47e7-88f5-207abb5b313f', 'cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', 'd5e3f1a4-9c4d-432f-b2a2-333fae32d8b5', 'Sim')",
        "INSERT INTO voting.vote(id, id_session, voted_by, vote) VALUES('5321454a-5397-4289-979a-08c2b78d6137', 'cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', 'a1b2c3d4-e5f6-7890-ab12-34cd567ef89a', 'Não')"
})
@WithMockUser(username = "foocitizen@gmail.com")
public class VotingReportTest extends TestContainerExtension {

    @Autowired
    private VotingReport votingReport;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void shouldCountVotes() {
        final CountReport count = votingReport.count("1111-2222-3333");
        Assertions.assertThat(count.isOpen()).isTrue();
        Assertions.assertThat(count.countNo()).isEqualTo(2L);
        Assertions.assertThat(count.countYes()).isEqualTo(2L);
        Assertions.assertThat(count.totalVotes()).isEqualTo(4L);
        Assertions.assertThat(count.percentYes()).isEqualTo("50%");
        Assertions.assertThat(count.percentNo()).isEqualTo("50%");
    }

    @Test
    @WithMockUser(username = "4382472347")
    public void shouldThrowIfAnyoneOutOfVoteTryToCount() {
        Assertions.assertThatThrownBy(() -> votingReport.count("1111-2222-3333"))
                .isInstanceOf(PermissionDeniedVotingSessionView.class)
                .hasMessage("You don't have permission to count votes if you didn't vote");
    }

}