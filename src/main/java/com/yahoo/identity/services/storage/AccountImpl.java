package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Map;

public class AccountImpl implements Account {

    private final Map<String, Object> data;

    public AccountImpl(@Nonnull Map<String, Object> data) {
        this.data = data;
    }

    @Nonnull
    @Override
    public String getUsername() {
        return data.get("id").toString();
    }

    @Nonnull
    @Override
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(Long.valueOf(data.get("create_ts").toString()));
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(Long.valueOf(data.get("update_ts").toString()));
    }

}
