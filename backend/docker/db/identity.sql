CREATE TABLE account(
    uid INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(10) NOT NULL,
    last_name VARCHAR(10) NOT NULL,
    email VARCHAR(256) NOT NULL,
    email_verified BOOLEAN NOT NULL,
    password_hash VARCHAR(512) NOT NULL,
    create_ts BIGINT NOT NULL,
    update_ts BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    block_until_ts BIGINT DEFAULT NULL,
    consecutive_fails INTEGER NOT NULL DEFAULT 0
);