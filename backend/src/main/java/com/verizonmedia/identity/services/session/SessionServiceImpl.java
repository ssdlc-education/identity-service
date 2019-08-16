// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.credential.CredentialService;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    final TokenService tokenService;
    final AccountService accountService;
    final CredentialService credentialService;
    final SystemService systemService;

    public SessionServiceImpl(
        @Nonnull TokenService tokenService,
        @Nonnull AccountService accountService,
        @Nonnull CredentialService credentialService,
        @Nonnull SystemService systemService) {
        this.tokenService = tokenService;
        this.accountService = accountService;
        this.credentialService = credentialService;
        this.systemService = systemService;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        Credential credential = credentialService.fromPassword(username, password);
        return new LoggedInSessionImpl(credential, accountService, tokenService, systemService);
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        Credential credential = credentialService.fromString(credStr);
        return new LoggedInSessionImpl(credential, accountService, tokenService, systemService);
    }

    @Override
    @Nonnull
    public Session newAnonymousSession() {
        return new SessionImpl(accountService);
    }
}
