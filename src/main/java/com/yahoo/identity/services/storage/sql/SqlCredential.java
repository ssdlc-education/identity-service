package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.credential.Credential;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlCredential implements Credential {
    private CredentialModel credential = new CredentialModel();

    public SqlCredential(@Nonnull String credentialString) throws IdentityException {
        this.credential.fromString(credentialString);
    }

    @Override
    @Nonnull
    public Instant getIssueTime() { return credential.getIssueTime(); }

    @Override
    @Nonnull
    public Instant getExpireTime() { return credential.getExpireTime(); }

    @Override
    @Nonnull
    public String getSubject() { return credential.getSubject(); }

    @Override
    @Nonnull
    public String toString() { return credential.toString(); }

    @Override
    @Nonnull
    public Boolean validate(){ return getExpireTime().compareTo(Instant.now()) > 1; }
}
