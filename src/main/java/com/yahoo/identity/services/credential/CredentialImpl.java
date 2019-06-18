package com.yahoo.identity.services.credential;

import javax.annotation.Nonnull;
import java.time.Instant;

public class CredentialImpl implements Credential {
    private String subject;
    private final Instant issueTime;
    private final Instant expireTime;

    public CredentialImpl (@Nonnull String subject, @Nonnull long issueTime, @Nonnull long expireTime) {
        this.subject = subject;
        this.issueTime = Instant.ofEpochMilli(issueTime);
        this.expireTime = Instant.ofEpochMilli(expireTime);
    }

    @Nonnull
    public Instant getIssueTime() {
        return this.issueTime;
    }

    @Nonnull
    public Instant getExpireTime() {
        return this.expireTime;
    }

    @Nonnull
    public String getSubject() {
        return this.subject;
    }

    @Override
    @Nonnull
    public String toString() {
        return this.subject;
    }

    public void validate() {}
}
