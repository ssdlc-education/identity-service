package com.yahoo.identity.services.token;

import com.yahoo.identity.services.key.KeyService;

import javax.annotation.Nonnull;

public class TokenServiceImpl implements TokenService {

    private final KeyService keyService;

    public TokenServiceImpl(@Nonnull KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImpl(this.keyService);
    }
}
