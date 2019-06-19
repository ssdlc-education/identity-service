package com.yahoo.identity.services.credential;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface CredentialCreate {

    @Nonnull
    CredentialCreate setIssueTime(Instant issueTime);

    @Nonnull
    CredentialCreate setExpireTime(Instant expireTime);

    @Nonnull
    CredentialCreate setSubject(String subject);
}
