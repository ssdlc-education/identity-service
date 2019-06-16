package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    AccountUpdate setBlockUntil(@Nonnull long blockUntil);

    @Nonnull
    AccountUpdate setNthTrial(@Nonnull int nthTrial);

    @Nonnull
    String update() throws IdentityException;
}