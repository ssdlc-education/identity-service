package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.token.TokenService;
import com.verizonmedia.identity.services.token.TokenType;

import javax.annotation.Nonnull;

public class SessionAccountUpdateImpl implements SessionAccountUpdate {

    final AccountUpdate accountUpdate;
    final TokenService tokenService;
    String tokenStr;
    TokenType tokenType = TokenType.STANDARD;

    public SessionAccountUpdateImpl(
        @Nonnull AccountUpdate accountUpdate,
        @Nonnull TokenService tokenService) {
        this.accountUpdate = accountUpdate;
        this.tokenService = tokenService;
    }

    @Nonnull
    @Override
    public SessionAccountUpdate setToken(@Nonnull String token) {
        tokenStr = token;
        return this;
    }

    @Nonnull
    @Override
    public SessionAccountUpdate setEmail(@Nonnull String email) {
        tokenType = TokenType.CRITICAL;
        accountUpdate.setEmail(email);
        return this;
    }

    @Nonnull
    @Override
    public SessionAccountUpdate setPassword(@Nonnull String password) {
        tokenType = TokenType.CRITICAL;
        accountUpdate.setPassword(password);
        return this;
    }

    @Nonnull
    @Override
    public SessionAccountUpdate setDescription(@Nonnull String description) {
        accountUpdate.setDescription(description);
        return this;
    }

    @Override
    public void update() throws IdentityException {
        preUpdateCheck();
        accountUpdate.update();
    }

    void preUpdateCheck() {
        Validate.notNull(tokenStr, "Token is required");
        tokenService.newTokenFromString(tokenStr);
    }
}
