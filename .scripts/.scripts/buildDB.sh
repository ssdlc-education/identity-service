sqlite3 identity.sqlite \
"CREATE TABLE account(uid INTEGER PRIMARY KEY, username VARCHAR(30) NOT NULL, first_name VARCHAR(10) NOT NULL, last_name VARCHAR(10) NOT NULL, email VARCHAR(256) NOT NULL, email_status TINYINT NOT NULL, password_hash VARBINARY(64) NOT NULL, password_salt VARBINARY(64) NOT NULL, create_ts BIGINT NOT NULL, update_ts BIGINT NOT NULL, description VARCHAR(500) NOT NULL, block_until_ts BIGINT NOT NULL, consecutive_fails INTEGER NOT NULL DEFAULT 0);"
echo "successfully create identity.sqlite"
