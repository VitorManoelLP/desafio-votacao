package com.voting.challenge.app.service;

import com.voting.challenge.domain.VotingSession;
import com.voting.challenge.domain.payload.Expiration;
import com.voting.challenge.domain.payload.RegisterSessionDTO;
import com.voting.challenge.domain.payload.RegisterSessionResponse;
import com.voting.challenge.enums.ExpirationType;
import com.voting.challenge.extension.TestContainerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Sql(statements = {
        "INSERT INTO keycloak.user_entity(id, username, email) VALUES('f3e15192-e428-4352-a240-bafa22eebd1e', 'foo', 'foo@gmail.com')"
})
@WithMockUser(username = "foo@gmail.com")
public class RegisterTopicTest extends TestContainerExtension {

    @Autowired
    private RegisterTopic registerTopic;

    @Test
    public void shouldInitTopicDefaultMinute() {

        final String description = "Should I approve a Foo request about something?";

        final RegisterSessionResponse sessionCode = registerTopic.init(new RegisterSessionDTO(description, Optional.of(new Expiration(1L, ExpirationType.MINUTES))));

        final VotingSession session = getEm().createQuery("SELECT session FROM VotingSession session WHERE session.code = :code", VotingSession.class)
                .setParameter("code", sessionCode.sessionCode())
                .getSingleResult();

        Assertions.assertThat(session.getTopic().getDescription()).isEqualTo(description);
        Assertions.assertThat(session.getCode()).containsPattern("\\d{4}-\\d{4}-\\d{4}");

        Assertions.assertThat(session.getStartTime().plusMinutes(1L))
                .usingComparator(Comparator.comparing(LocalDateTime::getMinute))
                .isEqualTo(session.getEndTime());
    }

}