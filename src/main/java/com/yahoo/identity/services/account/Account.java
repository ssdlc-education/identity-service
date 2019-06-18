package com.yahoo.identity.services.account;

import javax.annotation.Nonnull;
import java.time.Instant;

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
    Instant getBlockUntil();

    @Nonnull
    int getNthTrial();
}
