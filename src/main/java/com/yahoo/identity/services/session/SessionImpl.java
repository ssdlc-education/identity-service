package com.yahoo.identity.services.session;

import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.credential.CredentialImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class SessionImpl implements Session {

    private String username;
    private String password;
    private Credential credential = new CredentialImpl();

    public String getUsername() {
        return this.username;
    }

    public void setUsername(@Nonnull String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(@Nonnull String password) {
        this.password = password;
    }

    @Override
    @Nonnull
    public Credential getCredential() {
        return this.credential;
    }

    public void initCredential() {
        this.credential.setIssueTime(Instant.now());
        this.credential.setExpireTime(Instant.now().plus(7, ChronoUnit.DAYS));
        this.credential.setSubject(getUsername());
        this.credential.validate();
    }
}