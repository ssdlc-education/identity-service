package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class AccountServiceImplFixed extends AccountServiceImpl {

    public AccountServiceImplFixed(@Nonnull Storage storage,
                                   @Nonnull PasswordService passwordService,
                                   @Nonnull TokenService tokenService,
                                   @Nonnull SystemService systemService) {
        super(storage, passwordService, tokenService, systemService);
    }

    @Override
    @Nonnull
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return new AccountUpdateImplFixed(username, storage, systemService, passwordService, tokenService);
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
