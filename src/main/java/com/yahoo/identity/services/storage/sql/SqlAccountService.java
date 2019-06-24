package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class SqlAccountService implements AccountService {

    private final Storage storage;

    public SqlAccountService(@Nonnull Storage storage) {
        this.storage = storage;
    }

    @Override
    @Nonnull
    public AccountCreate newAccountCreate() {
        return this.storage.newAccountCreate();
    }

    @Override
    public Account getAccount(@Nonnull String username) {
        AccountModel accountModel = this.storage.getAccount(username);
        return new AccountImpl(accountModel);
    }

    @Nonnull
    @Override
    public Account getPublicAccount(@Nonnull String id) {
        return null;
    }

    @Override
    @Nonnull
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return this.storage.newAccountUpdate(username);
    }
}
