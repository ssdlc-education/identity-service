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
        return data.get("uid").toString();
    }

    @Nonnull
    @Override
    public String getUsername() {
        return data.get("username").toString();
    }

    @Nonnull
    @Override
    public String getFirstName() {
        return data.get("firstName").toString();
    }

    @Nonnull
    @Override
    public String getLastName() {
        return data.get("lastName").toString();
    }

    @Nonnull
    @Override
    public String getEmail() {
        return data.get("email").toString();
    }

    @Override
    public boolean isEmailVerified() {
        return (Boolean) data.get("emailStatus");
    }

    @Nonnull
    @Override
    public String getPassword() {
        return data.get("password").toString();
    }

    @Nonnull
    @Override
    public Instant getCreateTime() {
        return Instant.now();
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.now();
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
        return data.get("description").toString();
    }

    @Override
    @Nonnull
    public boolean verify(@Nonnull String password) {
        return false;
    }
}
