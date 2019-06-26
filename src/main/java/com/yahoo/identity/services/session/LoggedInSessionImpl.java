package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.credential.CredentialImpl;
import com.yahoo.identity.services.credential.CredentialService;
import com.yahoo.identity.services.credential.CredentialServiceImpl;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class LoggedInSessionImpl implements LoggedInSession {

    private CredentialService credentialService;
    private AccountService accountService;
    private Credential credential;
    private boolean verified;
    private String username;
    private String password;

    public LoggedInSessionImpl(@Nonnull Storage storage) {
        this.accountService = new SqlAccountService(storage);
        this.credentialService = new CredentialServiceImpl();
        this.credential = new CredentialImpl();
    }

    @Override
    @Nonnull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    public void setPassword(@Nonnull String password) {
        this.password = password;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void initCredential() {
        this.credential.setIssueTime(Instant.now());
        this.credential.setExpireTime(Instant.now().plus(7, ChronoUnit.DAYS));
        this.credential.setSubject(getUsername());
        this.credential.validate();
    }

    @Override
    @Nonnull
    public Credential getCredential() {
        return this.credential;
    }

    @Nonnull
    public Credential setCredential(@Nonnull String credStr) {
        this.credential = this.credentialService.fromString(credStr, "token");
        return this.credential;
    }

    @Override
    @Nonnull
    public Account getAccount() {
        return this.accountService.getAccount(getUsername());
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