package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;

import javax.annotation.Nonnull;

public class TokenServiceImpl implements TokenService {

    private static final String KEY_NAME = "identity-token";
    private final Algorithm algorithm;

    public TokenServiceImpl(@Nonnull KeyService keyService) {
        this.algorithm = createAlgorithm(keyService);
    }

    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.HMAC256(keyService.getHMACKey(KEY_NAME));
    }

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImpl(algorithm);
    }

    @Override
    @Nonnull
    public Token newTokenFromString(@Nonnull String tokenStr) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(tokenStr);
            return new TokenImpl.Builder()
                .setAlgorithm(algorithm)
                .setSubject(jwt.getSubject())
                .setIssueTime(jwt.getIssuedAt().toInstant())
                .setExpireTime(jwt.getExpiresAt().toInstant())
                .build();
        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }
}
