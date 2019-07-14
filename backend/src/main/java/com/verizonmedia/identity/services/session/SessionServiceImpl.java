package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.credential.CredentialService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    private final TokenService tokenService;
    private final AccountService accountService;
    private final CredentialService credentialService;

    public SessionServiceImpl(
        @Nonnull TokenService tokenService,
        @Nonnull AccountService accountService,
        @Nonnull CredentialService credentialService) {
        this.tokenService = tokenService;
        this.accountService = accountService;
        this.credentialService = credentialService;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        Credential credential = credentialService.fromPassword(username, password);
        return new LoggedInSessionImpl(credential, accountService, tokenService);
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        Credential credential = credentialService.fromString(credStr);
        return new LoggedInSessionImpl(credential, accountService, tokenService);
    }

    @Override
    @Nonnull
    public Session newAnonymousSession() {
        return new SessionImpl(accountService);
    }
}
