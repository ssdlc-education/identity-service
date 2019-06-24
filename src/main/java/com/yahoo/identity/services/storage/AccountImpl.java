package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;

import java.time.Instant;
import java.util.Map;

import javax.annotation.Nonnull;

public class AccountImpl implements Account {

    private final Map<String, Object> data;

    public AccountImpl(@Nonnull Map<String, Object> data) {
        this.data = data;
    }

    @Nonnull
    @Override
    public String getUid() {
        return "";
    }

    @Nonnull
    @Override
    public String getUsername() {
        return data.get("id").toString();
    }

    @Nonnull
    @Override
    public String getFirstName() {
        return "";
    }

    @Nonnull
    @Override
    public String getLastName() {
        return "";
    }

    @Nonnull
    @Override
    public String getEmail() {
        return "";
    }

    @Nonnull
    @Override
    public String getPassword() {
        return "";
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

    @Override
    @Nonnull
    public Instant getBlockUntilTime() {
        return Instant.now();
    }

    @Override
    public int getConsecutiveFails() {
        return 0;
    }

    @Nonnull
    @Override
    public String getDescription() {
        return "";
    }

    @Override
    @Nonnull
    public boolean verify(@Nonnull String password) {
        return false;
    }
}
