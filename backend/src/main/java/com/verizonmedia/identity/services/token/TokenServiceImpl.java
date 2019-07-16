package com.verizonmedia.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.system.SystemService;

import javax.annotation.Nonnull;

public class TokenServiceImpl implements TokenService {

    private static final String KEY_NAME = "identity-token";
    final Algorithm algorithm;
    final SystemService systemService;

    public TokenServiceImpl(@Nonnull KeyService keyService, @Nonnull SystemService systemService) {
        this.algorithm = createAlgorithm(keyService);
        this.systemService = systemService;
    }

    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.HMAC256(keyService.getHMACKey(KEY_NAME));
    }

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImpl(algorithm, systemService);
    }

    @Override
    @Nonnull
    public Token newTokenFromString(@Nonnull String tokenStr) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(tokenStr);
            return createTokenFromJWT(jwt);
        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }

    @Nonnull
    Token createTokenFromJWT(@Nonnull DecodedJWT jwt) {
        return new TokenImpl.Builder()
            .setSystemService(systemService)
            .setAlgorithm(algorithm)
            .setSubject(jwt.getSubject())
            .setIssueTime(jwt.getIssuedAt().toInstant())
            .setExpireTime(jwt.getIssuedAt().toInstant())
            .build();
    }
}
