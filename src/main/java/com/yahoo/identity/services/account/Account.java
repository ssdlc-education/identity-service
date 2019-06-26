package com.yahoo.identity.services.account;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Account {

    @Nonnull
    String getUid();

    @Nonnull
    String getUsername();

    @Nonnull
    String getFirstName();

    @Nonnull
    String getLastName();

    @Nonnull
    String getEmail();

    boolean getEmailStatus();

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
