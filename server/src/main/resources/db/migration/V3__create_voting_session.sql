CREATE TABLE IF NOT EXISTS  VOTING_SESSION (
    ID UUID PRIMARY KEY,
    ID_TOPIC UUID NOT NULL,
    START_TIME TIMESTAMP NOT NULL,
    END_TIME TIMESTAMP,
    CODE VARCHAR(14) NOT NULL UNIQUE,
    CONSTRAINT fk_topic_voting_session FOREIGN KEY (ID_TOPIC) REFERENCES TOPIC(ID)
);