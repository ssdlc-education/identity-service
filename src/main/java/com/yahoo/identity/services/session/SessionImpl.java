package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;

import javax.annotation.Nonnull;

public class SessionImpl implements Session {

    private AccountService accountService;

    public SessionImpl(@Nonnull Storage storage) {
        this.accountService = new SqlAccountService(storage);
    }

    @Override
    @Nonnull
    public Account getAccount(@Nonnull String username) {
        return this.accountService.getPublicAccount(username);
    }

    @Nonnull
    public AccountCreate sessionAccountCreate() {
        AccountCreate accountCreate = this.accountService.newAccountCreate();
        return accountCreate;
    }
}
