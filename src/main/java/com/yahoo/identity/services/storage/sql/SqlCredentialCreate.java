package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.credential.CredentialCreate;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlCredentialCreate implements CredentialCreate {
    private final CredentialModel credential = new CredentialModel();

    @Override
    @Nonnull
    public CredentialCreate setIssueTime(Instant issueTime){
        this.credential.setIssueTime(issueTime);
        return this;
    }

    @Override
    @Nonnull
    public CredentialCreate setExpireTime(Instant expireTime){
        this.credential.setExpireTime(expireTime);
        return this;
    }


    @Override
    @Nonnull
    public CredentialCreate setSubject(String subject){
        this.credential.setSubject(subject);
        return this;
    }


}
