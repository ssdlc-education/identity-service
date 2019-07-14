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
    AccountCreate setPassword(@Nonnull String password);

    @Nonnull
    AccountCreate setDescription(@Nonnull String description);

    @Nonnull
    String create() throws IdentityException;
}
