CREATE TABLE flows
(
    flow_id    VARCHAR(255) PRIMARY KEY NOT NULL,
    user_id    VARCHAR(255)             NOT NULL,
    challenge  VARCHAR(255)             NOT NULL,
    expired_at TIMESTAMP                NOT NULL,
    created_at TIMESTAMP                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP                NOT NULL DEFAULT CURRENT_TIMESTAMP
);