package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityException;

import javax.annotation.Nonnull;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    AccountUpdate setToken(@Nonnull String token);

    @Nonnull
    void update() throws IdentityException;
}