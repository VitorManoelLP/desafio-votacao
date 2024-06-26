CREATE SCHEMA IF NOT EXISTS keycloak;
CREATE TABLE keycloak.user_entity (
	id varchar(36) NOT NULL,
	email varchar(255) NULL,
	email_constraint varchar(255) NULL,
	email_verified bool DEFAULT false NOT NULL,
	enabled bool DEFAULT false NOT NULL,
	federation_link varchar(255) NULL,
	first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	realm_id varchar(255) NULL,
	username varchar(255) NULL,
	created_timestamp int8 NULL,
	service_account_client_link varchar(255) NULL,
	not_before int4 DEFAULT 0 NOT NULL,
	CONSTRAINT constraint_fb PRIMARY KEY (id),
	CONSTRAINT uk_dykn684sl8up1crfei6eckhd7 UNIQUE (realm_id, email_constraint),
	CONSTRAINT uk_ru8tt6t700s9v50bu18ws5ha6 UNIQUE (realm_id, username)
);
CREATE INDEX idx_user_email ON keycloak.user_entity USING btree (email);
CREATE INDEX idx_user_service_account ON keycloak.user_entity USING btree (realm_id, service_account_client_link);