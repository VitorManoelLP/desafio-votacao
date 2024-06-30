package com.voting.challenge.app.service;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import com.voting.challenge.app.interfaces.MarkConsultedSession;
import com.voting.challenge.app.repository.VotingSessionRepository;
import com.voting.challenge.domain.payload.SessionsByMember;
import com.voting.challenge.domain.payload.VotingSessionInfo;
import com.voting.challenge.enums.SessionSearchType;
import com.voting.challenge.extension.TestContainerExtension;

@Sql(statements = {
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('f3e15192-e428-4352-a240-bafa22eebd1e', 'foo', 'foo@gmail.com')",
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('cc4e1acc-830d-439d-9075-3323fa667cec', 'foo citizen', 'foocitizen@gmail.com')",
        "INSERT INTO voting.topic(id, description, id_owner) VALUES('338b005a-18bc-40b0-9fb5-7cf00edf4d3a', 'Yes or No?', 'f3e15192-e428-4352-a240-bafa22eebd1e')",
        "INSERT INTO voting.topic(id, description, id_owner) VALUES('0f528632-9c05-45b5-8f78-e5813e2fa612', 'Teste', 'f3e15192-e428-4352-a240-bafa22eebd1e')",
        """
        INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code)
        VALUES('cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', '338b005a-18bc-40b0-9fb5-7cf00edf4d3a', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-3333');
        """,
        """
        INSERT INTO voting.voting_session(id, id_topic, start_time, end_time, code)
        VALUES('81554842-4030-4e1a-b5a1-c06ee005e46c', '0f528632-9c05-45b5-8f78-e5813e2fa612', '2024-06-19 14:30:00', '2050-06-19 14:30:00', '1111-2222-4444');
        """,
        """
        INSERT INTO voting.vote(id, id_session, voted_by, vote) VALUES('5321454a-5397-4289-979a-08c2b78d6137', 'cb2e1a6d-7573-4a19-95c5-79bdc0a9925e', 'cc4e1acc-830d-439d-9075-3323fa667cec', 'Não');
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
    @WithMockUser(username = "cc4e1acc-830d-439d-9075-3323fa667cec")
    public void view() {
        VotingSessionInfo info = sessionInfo.view("1111-2222-3333");
        Assertions.assertThat(info.topic()).isEqualTo("Yes or No?");
        Assertions.assertThat(info.isOpen()).isTrue();
        Assertions.assertThat(info.openedAt()).isEqualTo(LocalDateTime.of(2024, 6, 19, 14, 30));
        Assertions.assertThat(info.closeAt()).isEqualTo(LocalDateTime.of(2050, 6, 19, 14, 30));
    }

    @Test
    public void byMember() {
        SessionsByMember sessionsByMember = sessionInfo.byMember();
        Assertions.assertThat(sessionsByMember.sessionsVotedCount()).isEqualTo(0L);
        Assertions.assertThat(sessionsByMember.sessionsOpenedCount()).isEqualTo(2L);
    }

    @Test
    public void getSessionPagedCreated() {

        final Page<VotingSessionInfo> sessions = sessionInfo.getSessions(SessionSearchType.CREATED, "", Pageable.ofSize(20));
        final Page<VotingSessionInfo> sessionsVoted = sessionInfo.getSessions(SessionSearchType.VOTED, "", Pageable.ofSize(20));

        Assertions.assertThat(sessionsVoted).isEmpty();

        Assertions.assertThat(sessions)
                .isNotEmpty()
                .hasSize(2);

        final VotingSessionInfo votingSessionInfo = sessions.getContent().get(0);
        Assertions.assertThat(votingSessionInfo.topic()).isEqualTo("Yes or No?");
        Assertions.assertThat(votingSessionInfo.code()).isEqualTo("1111-2222-3333");
        Assertions.assertThat(votingSessionInfo.isOpen()).isTrue();
        Assertions.assertThat(votingSessionInfo.alreadyVote()).isFalse();
        Assertions.assertThat(votingSessionInfo.yourVote()).isEmpty();
        Assertions.assertThat(votingSessionInfo.openedAt()).isEqualTo(LocalDateTime.of(2024, 6, 19, 14, 30));
        Assertions.assertThat(votingSessionInfo.closeAt()).isEqualTo(LocalDateTime.of(2050, 6, 19, 14, 30));
    }

    @Test
    @WithMockUser(username = "cc4e1acc-830d-439d-9075-3323fa667cec")
    public void getSessionPagedVoted() {

        final Page<VotingSessionInfo> sessions = sessionInfo.getSessions(SessionSearchType.VOTED, "", Pageable.ofSize(20));

        Assertions.assertThat(sessions)
                .isNotEmpty()
                .hasSize(1);

        final VotingSessionInfo votingSessionInfo = sessions.getContent().get(0);
        Assertions.assertThat(votingSessionInfo.topic()).isEqualTo("Yes or No?");
        Assertions.assertThat(votingSessionInfo.code()).isEqualTo("1111-2222-3333");
        Assertions.assertThat(votingSessionInfo.isOpen()).isTrue();
        Assertions.assertThat(votingSessionInfo.alreadyVote()).isTrue();
        Assertions.assertThat(votingSessionInfo.yourVote()).isEqualTo("Não");
        Assertions.assertThat(votingSessionInfo.openedAt()).isEqualTo(LocalDateTime.of(2024, 6, 19, 14, 30));
        Assertions.assertThat(votingSessionInfo.closeAt()).isEqualTo(LocalDateTime.of(2050, 6, 19, 14, 30));
    }

    @Test
    public void getSessionPagedCreatedFiltered() {

        final Page<VotingSessionInfo> sessions = sessionInfo.getSessions(SessionSearchType.CREATED, "yes or NO", Pageable.ofSize(20));

        Assertions.assertThat(sessions)
                .isNotEmpty()
                .hasSize(1);
    }

}
