package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class LoggedInSessionImplFixed extends LoggedInSessionImpl {

    public LoggedInSessionImplFixed(
        @Nonnull Credential credential,
        @Nonnull AccountService accountService,
        @Nonnull TokenService tokenService,
        @Nonnull SystemService systemService) {
        super(credential, accountService, tokenService, systemService);
    }

    @Override
    @Nonnull
    public SessionAccountUpdate newAccountUpdate() {
        AccountUpdate accountUpdate = accountService.newAccountUpdate(getUsername());
        return new SessionAccountUpdateImplFixed(
            accountUpdate,
            tokenService,
            credential,
            systemService
        );
    }
}
