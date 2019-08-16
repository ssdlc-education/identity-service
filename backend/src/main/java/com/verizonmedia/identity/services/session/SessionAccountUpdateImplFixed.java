// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.Token;
import com.verizonmedia.identity.services.token.TokenService;
import com.verizonmedia.identity.services.token.TokenType;

import java.time.Duration;

import javax.annotation.Nonnull;

public class SessionAccountUpdateImplFixed extends SessionAccountUpdateImpl {

    private final Credential credential;
    private final SystemService systemService;
    private TokenType tokenType = TokenType.STANDARD;

    public SessionAccountUpdateImplFixed(
        @Nonnull AccountUpdate accountUpdate,
        @Nonnull TokenService tokenService,
        @Nonnull Credential credential,
        @Nonnull SystemService systemService) {
        super(accountUpdate, tokenService);
        this.credential = credential;
        this.systemService = systemService;

    }

    @Nonnull
    @Override
    public SessionAccountUpdate setEmail(@Nonnull String email) {
        tokenType = TokenType.CRITICAL;
        return super.setEmail(email);
    }

    @Nonnull
    @Override
    public SessionAccountUpdate setPassword(@Nonnull String password) {
        tokenType = TokenType.CRITICAL;
        return super.setPassword(password);
    }

    @Override
    void preUpdateCheck() {
        checkSession();
        checkToken();
    }

    private void checkSession() {
        Duration sessionAge = Duration.between(credential.getIssueTime(), systemService.now());
        if (tokenType.needRelogin(sessionAge)) {
            throw new IdentityException(IdentityError.RELOGIN_REQUIRED, "Need to re-login");
        }
    }

    private void checkToken() {
        Validate.notNull(tokenStr, "Token is required");
        Token token = tokenService.newTokenFromString(tokenStr);
        if (!token.getSubject().equals(accountUpdate.getUsername())) {
            throw new IdentityException(IdentityError.INVALID_TOKEN,
                                        "Token's subject doesn't match the username");
        }
    }
}
