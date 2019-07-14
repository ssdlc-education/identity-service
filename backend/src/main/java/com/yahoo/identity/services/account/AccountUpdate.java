package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    void update() throws IdentityException;
}