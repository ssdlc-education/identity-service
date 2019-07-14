package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountService;

import javax.annotation.Nonnull;

public class SessionImpl implements Session {

    private AccountService accountService;

    public SessionImpl(@Nonnull AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @Nonnull
    public Account getAccount(@Nonnull String username) {
        return this.accountService.getPublicAccount(username);
    }

    @Nonnull
    public AccountCreate newAccountCreate() {
        return accountService.newAccountCreate();
    }
}
