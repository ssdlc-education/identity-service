package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.password.PasswordService;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;

import javax.annotation.Nonnull;

public class SqlAccountUpdate implements AccountUpdate {

    private Storage storage;
    private final SystemService systemService;
    private final PasswordService passwordService;
    private final AccountModel account = new AccountModel();

    public SqlAccountUpdate(
        @Nonnull String username,
        @Nonnull Storage storage,
        @Nonnull SystemService systemService,
        @Nonnull PasswordService passwordService) {
        this.storage = storage;
        this.systemService = systemService;
        this.passwordService = passwordService;
        this.account.setUsername(username);
    }

    @Override
    @Nonnull
    public AccountUpdate setEmail(@Nonnull String email) {
        account.setEmail(email);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setPassword(@Nonnull String password) {
        account.setPasswordHash(passwordService.createPasswordHash(password));
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setDescription(@Nonnull String title) {
        account.setDescription(title);
        return this;
    }

    @Nonnull
    @Override
    public void update() throws IdentityException {
        account.setUpdateTs(systemService.currentTimeMillis());
        storage.updateAccount(account);
    }
}
