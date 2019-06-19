package com.yahoo.identity.services.credential;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Credential {

    @Nonnull
    Instant getIssueTime();

    @Nonnull
    Instant getExpireTime();

    @Nonnull
    String getSubject();

    @Override
    @Nonnull
    String toString();

    @Nonnull
    Boolean validate();
}
