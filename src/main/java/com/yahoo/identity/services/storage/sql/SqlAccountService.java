package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class SqlAccountService implements AccountService {
    private final Storage storage;
    public SqlAccountService(@Nonnull Storage storage) { this.storage = storage; }

    @Override
    @Nonnull
    public AccountCreate newAccountCreate() {
        return this.storage.newAccountCreate();
    }

    @Override
    public Account getAccount(@Nonnull String username) {
        return this.storage.getAccount(username);
    }

    @Override
    @Nonnull
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return this.storage.newAccountUpdate(username);
    }
}
