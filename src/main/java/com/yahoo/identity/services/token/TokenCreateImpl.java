package com.yahoo.identity.services.token;

import com.yahoo.identity.services.key.KeyService;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class TokenCreateImpl implements TokenCreate {

    private final TokenImpl.Builder tokenBuilder;

    public TokenCreateImpl(@Nonnull KeyService keyService) {
        this.tokenBuilder = new TokenImpl.Builder()
            .setKeyService(keyService);
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
