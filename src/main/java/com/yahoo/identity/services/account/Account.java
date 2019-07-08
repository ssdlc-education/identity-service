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

    @Nonnull
    String getEmail();

    boolean isEmailVerified();

    @Nonnull
    String getPassword();

    @Nonnull
    Instant getCreateTime();

    @Nonnull
    Instant getUpdateTime();

    @Nonnull
    String getDescription();

    @Nonnull
    Instant getBlockUntilTime();

    int getConsecutiveFails();

    boolean verify(@Nonnull String password);
}
