package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email, @Nonnull Boolean verified);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    AccountUpdate setBlockUntil(@Nonnull Instant blockUntil);

    @Nonnull
    AccountUpdate setNthTrial(@Nonnull int nthTrial);

    @Nonnull
    String update() throws IdentityException;
}