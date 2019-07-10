package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;

import java.time.Instant;
import java.util.Map;

import javax.annotation.Nonnull;

public class AccountConvert implements Account {

    private final Map<String, Object> data;

    public AccountConvert(@Nonnull Map<String, Object> data) {
        this.data = data;
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

    @Override
    public String getEmail() {
        Object email = data.get("email");
        return email == null ? null : email.toString();
    }

    @Override
    public boolean isEmailVerified() {
        return (Boolean) data.get("emailStatus");
    }

    @Override
    public String getPassword() {
        Object password = data.get("password");
        return password == null ? null : password.toString();
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
    public String getDescription() {
        Object description = data.get("description");
        return description == null ? null : description.toString();
    }

    @Override
    public boolean verify(@Nonnull String password) {
        return false;
    }
}