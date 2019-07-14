package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class AccountUpdateImpl implements AccountUpdate {

    final Storage storage;
    final SystemService systemService;
    private final PasswordService passwordService;
    final AccountModel account = new AccountModel();
    final TokenService tokenService;
    String tokenStr;

    public AccountUpdateImpl(
        @Nonnull String username,
        @Nonnull Storage storage,
        @Nonnull SystemService systemService,
        @Nonnull PasswordService passwordService,
        @Nonnull TokenService tokenService) {
        this.storage = storage;
        this.systemService = systemService;
        this.passwordService = passwordService;
        this.tokenService = tokenService;
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
    public AccountUpdate setToken(@Nonnull String token) {
        this.tokenStr = token;
        return this;
    }

    @Nonnull
    @Override
    public void update() throws IdentityException {
        Validate.notNull(tokenStr, "Token is required");
        tokenService.newTokenFromString(tokenStr);
        account.setUpdateTs(systemService.currentTimeMillis());
        storage.updateAccount(account);
    }
}
