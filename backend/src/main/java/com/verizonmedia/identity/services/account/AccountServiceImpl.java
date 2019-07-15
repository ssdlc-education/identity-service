package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountImpl;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class AccountServiceImpl implements AccountService {

    final Storage storage;
    final PasswordService passwordService;
    final SystemService systemService;
    final TokenService tokenService;

    public AccountServiceImpl(@Nonnull Storage storage,
                              @Nonnull PasswordService passwordService,
                              @Nonnull TokenService tokenService,
                              @Nonnull SystemService systemService) {
        this.storage = storage;
        this.passwordService = passwordService;
        this.systemService = systemService;
        this.tokenService = tokenService;
    }

    @Override
    @Nonnull
    public AccountCreate newAccountCreate() {
        return new AccountCreateImpl(storage, passwordService, systemService);
    }

    @Override
    @Nonnull
    public Account getAccount(@Nonnull String username) {
        AccountModel accountModel = storage.getAccount(username);
        return new AccountImpl(accountModel);
    }

    @Override
    @Nonnull
    public Account getPublicAccount(@Nonnull String username) {
        AccountModel accountModel = storage.getPublicAccount(username);
        return new AccountImpl(accountModel);
    }

    @Override
    @Nonnull
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return new AccountUpdateImpl(username, storage, systemService, passwordService, tokenService);
    }

    @Override
    public void verifyAccountPassword(@Nonnull String username, @Nonnull String password) {
        AccountModel account = storage.getAccount(username);
        boolean verified = passwordService.verifyPasswordHash(password, account.getPasswordHash());
        if (!verified) {
            throw new IdentityException(IdentityError.INVALID_PASSWORD, "Invalid password");
        }
    }
}
