CREATE TABLE social_login_state
(
    state          VARCHAR(255) NOT NULL PRIMARY KEY,
    user_id        VARCHAR(255),
    challenge      TEXT,
    expired_at     TIMESTAMP,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
