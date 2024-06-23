package com.voting.challenge.extension;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

@SpringBootTest
@Testcontainers
@Transactional
@Rollback
@ActiveProfiles("test")
public abstract class TestContainerExtension {

    @Autowired
    private EntityManager em;

    @ServiceConnection
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withEnv(Map.of("PGDATA", "/var/lib/postgresql/data"))
            .withTmpFs(Map.of("/var/lib/postgresql/data", "rw"))
            .withReuse(true);

    public EntityManager getEm() {
        return em;
    }

    @BeforeAll
    public static void beforeAll() {
        postgresContainer.start();
    }

}
