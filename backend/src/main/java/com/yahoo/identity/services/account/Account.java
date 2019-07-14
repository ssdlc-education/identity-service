package com.yahoo.identity.services.account;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Account {

    @Nonnull
    String getUsername();

    @Nonnull
    String getFirstName();

    @Nonnull
    String getLastName();

    String getEmail();

    boolean isEmailVerified();

    String getPassword();

    @Nonnull
    Instant getCreateTime();

    @Nonnull
    Instant getUpdateTime();

    String getDescription();
}
