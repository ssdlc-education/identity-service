package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.Token;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class AccountUpdateImplFixed extends AccountUpdateImpl {

    public AccountUpdateImplFixed(
        @Nonnull String username,
        @Nonnull Storage storage,
        @Nonnull SystemService systemService,
        @Nonnull PasswordService passwordService,
        @Nonnull TokenService tokenService) {
        super(username, storage, systemService, passwordService, tokenService);
    }

    @Nonnull
    @Override
    public void update() throws IdentityException {
        Validate.notNull(tokenStr, "Token is required");
        Token token = tokenService.newTokenFromString(tokenStr);
        if (!account.getUsername().equals(token.getSubject())) {
            throw new IdentityException(IdentityError.INVALID_TOKEN,
                                        "Token's subject doesn't match the username");
        }
        account.setUpdateTs(systemService.currentTimeMillis());
        storage.updateAccount(account);
    }
}
