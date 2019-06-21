package com.yahoo.identity.services.storage.sql;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class SessionModel {
    private String username;
    private String password;
    private CredentialModel credential = new CredentialModel();

    public String getUsername() { return this.username; }

    public void setUsername(@Nonnull  String username) { this.username = username; }

    public String getPassword() { return this.password; }

    public void setPassword(@Nonnull  String password) { this.password = password; }

    public CredentialModel getCredential() { return this.credential; }

    public void setCredential(@Nonnull String credStr) { this.credential.fromString(credStr); }

    public void initCredential(){
        credential.setIssueTime(Instant.now());
        credential.setExpireTime(Instant.now().plus(7, ChronoUnit.DAYS));
        credential.setSubject(getUsername());
    }
}
