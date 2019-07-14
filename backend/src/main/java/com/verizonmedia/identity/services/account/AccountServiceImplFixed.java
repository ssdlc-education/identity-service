package com.verizonmedia.identity.services.account;

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
}
