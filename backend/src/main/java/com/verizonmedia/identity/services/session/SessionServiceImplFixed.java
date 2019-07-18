package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.credential.CredentialService;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class SessionServiceImplFixed extends SessionServiceImpl {

    public SessionServiceImplFixed(@Nonnull TokenService tokenService,
                                   @Nonnull AccountService accountService,
                                   @Nonnull CredentialService credentialService,
                                   @Nonnull SystemService systemService) {
        super(tokenService, accountService, credentialService, systemService);
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        Credential credential = credentialService.fromPassword(username, password);
        return new LoggedInSessionImplFixed(credential, accountService, tokenService, systemService);
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        Credential credential = credentialService.fromString(credStr);
        return new LoggedInSessionImplFixed(credential, accountService, tokenService, systemService);
    }
}
