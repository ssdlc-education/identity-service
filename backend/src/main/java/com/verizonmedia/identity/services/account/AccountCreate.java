// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityException;

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
