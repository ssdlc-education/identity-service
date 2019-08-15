// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.storage;

import com.verizonmedia.identity.services.account.Account;

import java.time.Instant;

import javax.annotation.Nonnull;

public class AccountImpl implements Account {
    private final AccountModel accountModel;

    public AccountImpl(@Nonnull AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    @Nonnull
    @Override
    public String getUsername() {
        return this.accountModel.getUsername();
    }

    @Nonnull
    @Override
    public String getFirstName() {
        return this.accountModel.getFirstName();
    }

    @Nonnull
    @Override
    public String getLastName() {
        return this.accountModel.getLastName();
    }

    @Nonnull
    @Override
    public String getEmail() {
        return this.accountModel.getEmail();
    }

    @Override
    public boolean isEmailVerified() {
        return this.accountModel.isEmailVerified();
    }

    @Nonnull
    @Override
    public String getPassword() {
        return this.accountModel.getPasswordHash();
    }

    @Nonnull
    @Override
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(this.accountModel.getCreateTs());
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(this.accountModel.getUpdateTs());
    }


    @Nonnull
    @Override
    public String getDescription() {
        return this.accountModel.getDescription();
    }
}