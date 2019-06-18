sqlite3 identity.sqlite \
"CREATE TABLE account(uid INTEGER PRIMARY KEY, firstname VARCHAR(20), lastname VARCHAR(20), username VARCHAR(20), email VARCHAR(20), verified BOOLEAN, password VARCHAR(30), create_ts BIGINT, update_ts BIGINT, description VARCHAR, block_until BIGINT, nth_trial INTEGER);"
echo "successfully create identity.sqlite"
