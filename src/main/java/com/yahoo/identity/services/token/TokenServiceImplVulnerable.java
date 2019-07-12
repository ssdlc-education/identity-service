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

public class TokenServiceImplVulnerable implements TokenService {

    private final KeyService keyService;

    public TokenServiceImplVulnerable(@Nonnull KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    @Nonnull
    public TokenCreate newTokenCreate() {
        return new TokenCreateImpl(this.keyService);
    }

    @Override
    @Nonnull
    public Token newTokenFromString(@Nonnull String tokenStr) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.keyService.getSecret("token.key"));
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(tokenStr);

            return new TokenImpl.Builder()
                .setKeyService(keyService)
                .setSubject(jwt.getSubject())
                .setIssueTime(jwt.getIssuedAt().toInstant())
                .setExpireTime(jwt.getExpiresAt().toInstant())
                .build();
        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }

}
