package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.Validate;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.password.PasswordService;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;

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
    public AccountCreate setDescription(@Nonnull String title) {
        account.setDescription(title);
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

        account.setCreateTs(systemService.currentTimeMillis());
        account.setUpdateTs(systemService.currentTimeMillis());
        account.setEmailVerified(false);

        storage.createAccount(account);
        return account.getUsername();
    }
}
