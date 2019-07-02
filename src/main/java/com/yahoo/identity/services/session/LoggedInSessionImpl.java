package com.yahoo.identity.services.session;

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

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class LoggedInSessionImpl implements LoggedInSession {

    private CredentialService credentialService;
    private AccountService accountService;
    private Credential credential;
    private String username;

    public LoggedInSessionImpl(@Nonnull Storage storage, @Nonnull KeyService keyService) {
        this.accountService = new SqlAccountService(storage);
        this.credentialService = new CredentialServiceImpl(keyService);
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
            throw new NotAuthorizedException("Account is locked!");
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
    public LoggedInSessionImpl sessionAccountUpdate(@Nonnull Account account) {
        final boolean mockEmailStatus = true;

        AccountUpdate accountUpdate = this.accountService.newAccountUpdate(account.getUsername());
        accountUpdate.setEmail(account.getEmail());
        accountUpdate.setEmailStatus(mockEmailStatus);
        accountUpdate.setPassword(account.getPassword());
        accountUpdate.setDescription(account.getDescription());
        accountUpdate.setUpdateTime(Instant.now());
        accountUpdate.update();

        return this;
    }
}