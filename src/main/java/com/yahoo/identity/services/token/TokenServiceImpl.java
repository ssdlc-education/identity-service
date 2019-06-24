package com.yahoo.identity.services.token;

import javax.annotation.Nonnull;

public class TokenServiceImpl implements TokenService {

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImpl();
    }
}
