package com.yahoo.identity.services.credential;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface CredentialCreate {
    @Nonnull
    CredentialCreate setIssueTime(Instant issueTime);

    @Nonnull
    CredentialCreate setExpireTime(Instant expireTime);

    @Nonnull
    CredentialCreate setSubject(String subject);
}
