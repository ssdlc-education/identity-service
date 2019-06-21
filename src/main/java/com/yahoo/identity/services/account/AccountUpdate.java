package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email, @Nonnull Boolean verified);

    @Nonnull
    AccountUpdate setEmailStatus(@Nonnull int emailStatus);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    AccountUpdate setBlockUntilTime(@Nonnull Instant blockUntil);

    @Nonnull
    AccountUpdate setConsecutiveFails(@Nonnull int consecutiveFails);

    @Nonnull
    String update() throws IdentityException;
}