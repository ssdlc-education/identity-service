package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface AccountUpdate {
    @Nonnull
    AccountUpdate setFirstName(@Nonnull String firstName);

    @Nonnull
    AccountUpdate setLastName(@Nonnull String lastName);

    @Nonnull
    AccountUpdate setUsername(@Nonnull String username);

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setCreateTime(@Nonnull Instant createTime);

    @Nonnull
    AccountUpdate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    String update() throws IdentityException;
}