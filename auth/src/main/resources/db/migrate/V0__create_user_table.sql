CREATE TABLE users
(
    user_id            VARCHAR(255) PRIMARY KEY,
    username           VARCHAR(255) NOT NULL,
    password           VARCHAR(255) NOT NULL,
    is_account_lock    BOOLEAN      NOT NULL,
    failed_attempts    INT          NOT NULL,
    is_disabled        BOOLEAN      NOT NULL,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_username ON users(username);
