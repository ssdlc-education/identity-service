package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface AccountCreate {

    @Nonnull
    AccountCreate setFirstName(@Nonnull String firstName);

    @Nonnull
    AccountCreate setLastName(@Nonnull String lastName);

    @Nonnull
    AccountCreate setUsername(@Nonnull String username);

    @Nonnull
    AccountCreate setEmail(@Nonnull String email);

    @Nonnull
    AccountCreate setEmailStatus(@Nonnull boolean emailStatus);

    @Nonnull
    AccountCreate setPassword(@Nonnull String password);

    @Nonnull
    AccountCreate setCreateTime(@Nonnull Instant createTime);

    @Nonnull
    AccountCreate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountCreate setDescription(@Nonnull String description);

    @Nonnull
    String create() throws IdentityException;
}
