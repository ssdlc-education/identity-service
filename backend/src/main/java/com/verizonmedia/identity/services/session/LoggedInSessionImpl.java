// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.account.Account;
import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.Token;
import com.verizonmedia.identity.services.token.TokenService;
import com.verizonmedia.identity.services.token.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import javax.annotation.Nonnull;

public class LoggedInSessionImpl implements LoggedInSession {

    final AccountService accountService;
    final TokenService tokenService;
    final Credential credential;
    final SystemService systemService;

    public LoggedInSessionImpl(
        @Nonnull Credential credential,
        @Nonnull AccountService accountService,
        @Nonnull TokenService tokenService,
        @Nonnull SystemService systemService) {
        this.accountService = accountService;
        this.tokenService = tokenService;
        this.credential = credential;
        this.systemService = systemService;
    }

    @Nonnull
    public String getUsername() {
        return this.credential.getSubject();
    }

    @Override
    @Nonnull
    public Credential getCredential() {
        return this.credential;
    }

    @Override
    public void verifyPassword(@Nonnull String password) {
        accountService.verifyAccountPassword(getUsername(), password);
    }

    @Override
    @Nonnull
    public Account getAccount() {
        return this.accountService.getAccount(getUsername());
    }

    @Override
    @Nonnull
    public SessionAccountUpdate newAccountUpdate() {
        AccountUpdate accountUpdate = accountService.newAccountUpdate(getUsername());
        return new SessionAccountUpdateImpl(
            accountUpdate,
            tokenService
        );
    }

    @Nonnull
    @Override
    public Token createToken(@Nonnull TokenType tokenType) {
        Duration sessionAge = Duration.between(credential.getIssueTime(), systemService.now());
        if (tokenType.needRelogin(sessionAge)) {
            throw new IdentityException(IdentityError.RELOGIN_REQUIRED, "Need to login again to get token");
        }
        return tokenService.newTokenCreate()
            .setIssueTime(systemService.now())
            .setExpireTime(credential.getExpireTime())
            .setUsername(getUsername())
            .create();
    }
}