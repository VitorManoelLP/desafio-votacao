package com.voting.challenge.app.service;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import com.voting.challenge.app.interfaces.MarkConsultedSession;
import com.voting.challenge.app.repository.LastConsultedSessionRepository;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;
import com.voting.challenge.extension.TestContainerExtension;

@Sql(statements = {
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('f3e15192-e428-4352-a240-bafa22eebd1e', 'foo', 'foo@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('cc4e1acc-830d-439d-9075-3323fa667cec', 'foo citizen', 'foocitizen@gmail.com')",
        "INSERT INTO voting.topic(id, description, id_owner) VALUES('338b005a-18bc-40b0-9fb5-7cf00edf4d3a', 'Yes or No?', 'f3e15192-e428-4352-a240-bafa22eebd1e')",
        """
        INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code)
        VALUES('cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-3333');
        """
})
@WithMockUser(username = "f3e15192-e428-4352-a240-bafa22eebd1e")
public class SessionInfoTest extends TestContainerExtension {

    private final MarkConsultedSession markConsultedSession = Mockito.mock(MarkConsultedSession.class);
    private SessionInfo sessionInfo;

    @Autowired
    private VotingSessionRepository votingSessionRepository;

    @BeforeEach
    public void setup() {
        sessionInfo = new SessionInfo(
                votingSessionRepository,
                markConsultedSession
        );
    }

    @Test
    public void view() {
        VotingSessionInfo info = sessionInfo.view("1111-2222-3333");
        Assertions.assertThat(info.topic()).isEqualTo("Yes or No?");
        Assertions.assertThat(info.isOpen()).isEqualTo("Sim");
        Assertions.assertThat(info.openedAt()).isEqualTo(LocalDateTime.of(2024, 6, 19, 14, 30));
        Assertions.assertThat(info.closeAt()).isEqualTo(LocalDateTime.of(2050, 6, 19, 14, 30));
    }

    @Test
    public void byMember() {
        SessionsByMember sessionsByMember = sessionInfo.byMember();
        Assertions.assertThat(sessionsByMember.sessionsOpened()).hasSize(1);
        Assertions.assertThat(sessionsByMember.sessionsVoted()).isEmpty();
        Assertions.assertThat(sessionsByMember.sessionsVotedCount()).isEqualTo(0L);
        Assertions.assertThat(sessionsByMember.sessionsOpenedCount()).isEqualTo(1L);
    }
}
