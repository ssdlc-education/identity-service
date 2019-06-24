package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;

import java.time.Instant;
import java.util.Map;

import javax.annotation.Nonnull;

public class AccountImpl implements Account {

    private final AccountModel data;

    public AccountImpl(@Nonnull AccountModel data) {
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
        return data.getUsername();
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
        return Instant.ofEpochMilli(data.getCreateTs());
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(data.getUpdateTs());
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
    public Boolean verify(@Nonnull String password) {
        return false;
    }
}
