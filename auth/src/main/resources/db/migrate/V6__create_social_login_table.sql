CREATE TABLE social_login
(
    user_id        VARCHAR(255) NOT NULL,
    provider       VARCHAR(255) NOT NULL,
    sub            VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, provider),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
