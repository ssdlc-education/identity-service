package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.IdentityException;

import javax.annotation.Nonnull;

public interface SessionAccountUpdate {
    @Nonnull
    SessionAccountUpdate setEmail(@Nonnull String email);

    @Nonnull
    SessionAccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    SessionAccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    SessionAccountUpdate setToken(@Nonnull String token);

    @Nonnull
    void update() throws IdentityException;
}
