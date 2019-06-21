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

    @Nonnull
    int getConsecutiveFails();

    @Nonnull
    Boolean verify(@Nonnull String password);
}
