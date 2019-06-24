package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.credential.CredentialImpl;
import com.yahoo.identity.services.credential.CredentialService;
import com.yahoo.identity.services.credential.CredentialServiceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class LoggedInSessionImpl implements LoggedInSession {

    private final AccountService accountService;

    public LoggedInSessionImpl(@Nonnull AccountService accountService) {
        this.accountService = accountService;
    }

    private String username;

    public String getUsername() {
        return this.username;
    }

    @Nonnull
    @Override
    public Instant getIssueTime() {
        return null;
    }

    @Nonnull
    @Override
    public Instant getExpiryTime() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @Nonnull
    @Override
    public String getCredential() {
        return null;
    }

    @Nonnull
    @Override
    public Account getAccount() {
        return accountService.getAccount(username);
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    @Nonnull
    @Override
    public Account getAccount(String username) {
        return accountService.getPublicAccount(username);
    }
}
