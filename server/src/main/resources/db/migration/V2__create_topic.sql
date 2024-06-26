CREATE TABLE IF NOT EXISTS  TOPIC (
    ID UUID PRIMARY KEY,
    DESCRIPTION VARCHAR(4000) NOT NULL,
    ID_OWNER VARCHAR(36) NOT NULL,
    CONSTRAINT chk_description_empty CHECK (TRIM(DESCRIPTION) <> ''),
    CONSTRAINT fk_owner_member_topic FOREIGN KEY (ID_OWNER) REFERENCES KEYCLOAK.USER_ENTITY(ID)
);