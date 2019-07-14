package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.password.PasswordService;
import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.AccountCreateImpl;
import com.yahoo.identity.services.storage.sql.SqlAccountUpdate;
import com.yahoo.identity.services.system.SystemService;

import javax.annotation.Nonnull;

public class AccountServiceImpl implements AccountService {

    private final Storage storage;
    private final PasswordService passwordService;
    private final SystemService systemService;

    public AccountServiceImpl(@Nonnull Storage storage,
                              @Nonnull PasswordService passwordService,
                              @Nonnull SystemService systemService) {
        this.storage = storage;
        this.passwordService = passwordService;
        this.systemService = systemService;
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
        return new SqlAccountUpdate(username, storage, systemService, passwordService);
    }

    @Override
    public void verifyAccountPassword(@Nonnull String username, @Nonnull String password) {
        AccountPasswordVerifier verifier = new AccountPasswordVerifier(
            password,
            passwordService,
            systemService);
        storage.getAndUpdateAccount(username, verifier);
        if (!verifier.isVerified()) {
            throw new IdentityException(IdentityError.INVALID_PASSWORD, "Invalid password");
        }
    }
}
