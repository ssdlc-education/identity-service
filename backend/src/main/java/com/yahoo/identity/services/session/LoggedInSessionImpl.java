package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.token.Token;
import com.yahoo.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class LoggedInSessionImpl implements LoggedInSession {

    private final AccountService accountService;
    private final TokenService tokenService;
    private final Credential credential;

    public LoggedInSessionImpl(
        @Nonnull Credential credential,
        @Nonnull AccountService accountService,
        @Nonnull TokenService tokenService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.credential = credential;
    }

    @Nonnull
    public String getUsername() {
        return this.credential.getSubject();
    }

    @Override
    @Nonnull
    public Credential getCredential() {
        return this.credential;
    }

    @Override
    public void verifyPassword(@Nonnull String password) {
        accountService.verifyAccountPassword(getUsername(), password);
    }

    @Override
    @Nonnull
    public Account getAccount() {
        return this.accountService.getAccount(getUsername());
    }

    @Override
    @Nonnull
    public AccountUpdate newAccountUpdate() {
        return accountService.newAccountUpdate(getUsername());
    }

    @Nonnull
    @Override
    public Token createToken() {
        return tokenService.newTokenCreate()
            .setExpireTime(credential.getExpireTime())
            .setUsername(getUsername())
            .create();
    }

    @Override
    public void validateTokenString(@Nonnull String tokenStr) {
        Token token = tokenService.newTokenFromString(tokenStr);
        token.validate();
    }
}