package com.yahoo.identity.services.credential;

import javax.annotation.Nonnull;
import java.time.Instant;

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

    void validate();
}
