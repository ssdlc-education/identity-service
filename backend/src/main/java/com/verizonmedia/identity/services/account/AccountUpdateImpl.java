package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;

import javax.annotation.Nonnull;

public class AccountUpdateImpl implements AccountUpdate {

    private final Storage storage;
    private final SystemService systemService;
    private final PasswordService passwordService;
    final AccountModel account = new AccountModel();

    public AccountUpdateImpl(
        @Nonnull String username,
        @Nonnull Storage storage,
        @Nonnull SystemService systemService,
        @Nonnull PasswordService passwordService) {
        this.storage = storage;
        this.systemService = systemService;
        this.passwordService = passwordService;
        this.account.setUsername(username);
    }

    @Nonnull
    @Override
    public String getUsername() {
        return account.getUsername();
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

    @Override
    public void update() throws IdentityException {
        account.setUpdateTs(systemService.currentTimeMillis());
        storage.updateAccount(account);
    }
}
