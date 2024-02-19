CREATE TABLE profiles
(
    user_id        VARCHAR(255),
    family_name    VARCHAR(255) NOT NULL,
    given_name     VARCHAR(255) NOT NULL,
    nickname       VARCHAR(255),
    picture        VARCHAR(255),
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
