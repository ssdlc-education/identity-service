package com.verizonmedia.identity.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.verizonmedia.identity.services.system.SystemService;

import java.time.Instant;

import javax.annotation.Nonnull;

public class TokenCreateImplFixed implements TokenCreate {

    private final TokenImplFixed.Builder tokenBuilder;

    public TokenCreateImplFixed(@Nonnull Algorithm algorithm, @Nonnull SystemService systemService) {
        this.tokenBuilder = new TokenImplFixed.Builder()
            .setAlgorithm(algorithm)
            .setSystemService(systemService);
    }

    @Override
    @Nonnull
    public TokenCreate setUsername(@Nonnull String username) {
        tokenBuilder.setSubject(username);
        return this;
    }

    @Nonnull
    @Override
    public TokenCreate setIssueTime(@Nonnull Instant issueTime) {
        tokenBuilder.setIssueTime(issueTime);
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
