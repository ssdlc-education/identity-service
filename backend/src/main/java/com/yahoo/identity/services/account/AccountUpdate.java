package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface AccountUpdate {

    @Nonnull
    AccountUpdate setEmail(@Nullable String email);

    @Nonnull
    AccountUpdate setEmailStatus(@Nonnull boolean emailStatus);

    @Nonnull
    AccountUpdate setPassword(@Nonnull String password);

    @Nonnull
    AccountUpdate setDescription(@Nonnull String description);

    @Nonnull
    String update() throws IdentityException;
}