package com.yahoo.identity.services.token;

import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;

import javax.annotation.Nonnull;

public class TokenCreateImpl implements TokenCreate {

    private final TokenImpl.Builder tokenBuilder;

    public TokenCreateImpl(@Nonnull Algorithm algorithm) {
        this.tokenBuilder = new TokenImpl.Builder()
            .setAlgorithm(algorithm);
    }

    @Override
    @Nonnull
    public TokenCreate setUsername(@Nonnull String username) {
        tokenBuilder.setSubject(username);
        return this;
    }

    @Override
    @Nonnull
    public TokenCreate setExpireTime(@Nonnull Instant expiryTime) {
        tokenBuilder.setExpireTime(expiryTime);
        return this;
    }

    @Override
    @Nonnull
    public Token create() {
        tokenBuilder.setIssueTime(Instant.now());
        return tokenBuilder.build();
    }
}
