package com.yahoo.identity.services.session;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.credential.CredentialImpl;
import com.yahoo.identity.services.credential.CredentialService;
import com.yahoo.identity.services.credential.CredentialServiceImpl;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;
import com.yahoo.identity.services.token.Token;
import com.yahoo.identity.services.token.TokenService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class LoggedInSessionImpl implements LoggedInSession {

    private final CredentialService credentialService;
    private final AccountService accountService;
    private final TokenService tokenService;
    private Credential credential;
    private String username;

    public LoggedInSessionImpl(
        @Nonnull Storage storage,
        @Nonnull KeyService keyService,
        @Nonnull TokenService tokenService) {
        this.accountService = new SqlAccountService(storage);
        this.credentialService = new CredentialServiceImpl(keyService);
        this.tokenService = tokenService;
        this.credential = new CredentialImpl(keyService);
    }

    public void initCredential() {
        this.credential.setIssueTime(Instant.now());
        this.credential.setExpireTime(Instant.now().plus(7, ChronoUnit.DAYS));
        this.credential.setSubject(this.username);
        this.credential.validate();
    }

    @Nonnull
    public String getUsername() {
        return this.username;
    }

    @Override
    @Nonnull
    public Credential getCredential() {
        return this.credential;
    }

    @Nonnull
    public Credential setCredential(@Nonnull String credStr) {
        this.credential = this.credentialService.fromString(credStr);
        this.username = this.credential.getSubject();
        return this.credential;
    }

    @Nonnull
    public Credential verifyPassword(@Nonnull String username, @Nonnull String password) {
        this.username = username;
        if (!getAccount().verify(password)) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL,
                                        "Account locked, or username and password do not match.");
        }
        initCredential();
        return this.credential;
    }

    @Override
    @Nonnull
    public Account getAccount() {
        return this.accountService.getAccount(this.username);
    }

    @Override
    @Nonnull
    public AccountUpdate sessionAccountUpdate() {
        return accountService.newAccountUpdate(this.username);
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