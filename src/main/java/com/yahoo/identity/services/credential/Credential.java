package com.yahoo.identity.services.credential;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Credential {

    @Nonnull
    Instant getIssueTime();

    void setIssueTime(@Nonnull Instant issueTime);

    @Nonnull
    Instant getExpireTime();

    void setExpireTime(@Nonnull Instant expireTime);

    @Nonnull
    String getSubject();

    void setSubject(@Nonnull String subject);

    @Nonnull
    boolean getStatus();

    void setStatus(boolean status);

    @Nonnull
    String toString();

    void validate();
}
