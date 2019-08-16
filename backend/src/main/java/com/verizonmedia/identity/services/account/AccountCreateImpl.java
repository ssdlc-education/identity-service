// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;

import javax.annotation.Nonnull;


public class AccountCreateImpl implements AccountCreate {

    private final Storage storage;
    private final SystemService systemService;
    private final PasswordService passwordService;
    private final AccountModel account = new AccountModel();

    public AccountCreateImpl(
        @Nonnull Storage storage,
        @Nonnull PasswordService passwordService,
        @Nonnull SystemService systemService) {
        this.storage = storage;
        this.passwordService = passwordService;
        this.systemService = systemService;
    }

    @Override
    @Nonnull
    public AccountCreate setUsername(@Nonnull String username) {
        account.setUsername(username);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setFirstName(@Nonnull String firstName) {
        account.setFirstName(firstName);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setLastName(@Nonnull String lastName) {
        account.setLastName(lastName);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setEmail(@Nonnull String email) {
        account.setEmail(email);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setPassword(@Nonnull String password) {
        account.setPasswordHash(passwordService.createPasswordHash(password));
        return this;
    }

    @Nonnull
    @Override
    public AccountCreate setDescription(@Nonnull String description) {
        account.setDescription(description);
        return this;
    }

    @Nonnull
    @Override
    public String create() throws IdentityException {
        Validate.notNull(account.getUsername(), "username must be set");
        Validate.notNull(account.getFirstName(), "first name must be set");
        Validate.notNull(account.getLastName(), "last name must be set");
        Validate.notNull(account.getPasswordHash(), "password must be set");
        Validate.notNull(account.getEmail(), "email must be set");
        Validate.notNull(account.getDescription(), "description must be set");

        long nowMs = systemService.currentTimeMillis();
        account.setCreateTs(nowMs);
        account.setUpdateTs(nowMs);
        account.setEmailVerified(false);

        storage.createAccount(account);
        return account.getUsername();
    }
}
