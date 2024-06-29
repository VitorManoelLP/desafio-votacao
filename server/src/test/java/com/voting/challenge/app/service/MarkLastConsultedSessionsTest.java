package com.voting.challenge.app.service;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import com.voting.challenge.app.repository.LastConsultedSessionRepository;
import com.voting.challenge.domain.LastConsultedSessions;
import com.voting.challenge.extension.TestContainerExtension;

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
        VALUES('9c1dfd8f-71e4-4488-852e-267102d462f7', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-4444');
        """
})
@WithMockUser(username = "f3e15192-e428-4352-a240-bafa22eebd1e")
public class MarkLastConsultedSessionsTest extends TestContainerExtension {

    @Autowired
    private MarkLastConsultedSessions markLastConsultedSessions;

    @Autowired
    private LastConsultedSessionRepository lastConsultedSessionRepository;

    @Test
    public void shouldSaveNewLastWhenNotExistsAnotherByUser() {
        markLastConsultedSessions.mark(UUID.fromString("cb2e1a6d-7573-4a19-95c5-79bdc0a9925e"));
        final Optional<LastConsultedSessions> lastConsultByLoggedUser = lastConsultedSessionRepository.findLastConsultByLoggedUser();
        Assertions.assertThat(lastConsultByLoggedUser)
                .isPresent()
                .get()
                .extracting(last -> last.getViewer().getId(), last -> last.getVotingSession().getId())
                .containsExactlyInAnyOrder(
                        "f3e15192-e428-4352-a240-bafa22eebd1e",
                        UUID.fromString("cb2e1a6d-7573-4a19-95c5-79bdc0a9925e"));
    }

    @Test
    public void shouldEditLastWhenAlreadyExistsOne() {
        markLastConsultedSessions.mark(UUID.fromString("cb2e1a6d-7573-4a19-95c5-79bdc0a9925e"));
        markLastConsultedSessions.mark(UUID.fromString("9c1dfd8f-71e4-4488-852e-267102d462f7"));
        final Optional<LastConsultedSessions> lastConsultByLoggedUser = lastConsultedSessionRepository.findLastConsultByLoggedUser();
        Assertions.assertThat(lastConsultByLoggedUser)
                .isPresent()
                .get()
                .extracting(last -> last.getViewer().getId(), last -> last.getVotingSession().getId())
                .containsExactlyInAnyOrder(
                        "f3e15192-e428-4352-a240-bafa22eebd1e",
                        UUID.fromString("9c1dfd8f-71e4-4488-852e-267102d462f7"));

    }

}