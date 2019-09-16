// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.system.SystemService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.annotation.Nonnull;

public class TokenServiceImplFixed extends TokenServiceImpl {

    private static final String KEY_NAME = "identity-token";

    public TokenServiceImplFixed(@Nonnull KeyService keyService, @Nonnull SystemService systemService) {
        super(keyService, systemService);
    }

    @Override
    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.RSA256((RSAPublicKey) keyService.getPublicKey(KEY_NAME, "RSA"),
                                (RSAPrivateKey) keyService.getPrivateKey(KEY_NAME, "RSA"));
    }

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImplFixed(algorithm, systemService);
    }

    @Nonnull
    Token createTokenFromJWT(@Nonnull DecodedJWT jwt) {
        return new TokenImplFixed.Builder()
            .setSystemService(systemService)
            .setAlgorithm(algorithm)
            .setSubject(jwt.getSubject())
            .setIssueTime(jwt.getIssuedAt().toInstant())
            .setExpireTime(jwt.getExpiresAt().toInstant())
            .build();
    }
}
