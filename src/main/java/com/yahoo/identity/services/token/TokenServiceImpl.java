package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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

    @Override
    @Nonnull
    public Token setToken(@Nonnull String tokenStr) {
        Token token = new TokenImpl(this.keyService);
        try {
            Algorithm algorithm =
                Algorithm.RSA256((RSAPublicKey) this.keyService.getPublicKey("token-public.pem", "RSA"),
                                 (RSAPrivateKey) this.keyService.getPrivateKey("token-private.pem", "RSA"));
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(tokenStr);

            token.setSubject(jwt.getSubject());
            token.setIssueTime(jwt.getIssuedAt().toInstant());
            token.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
        return token;
    }
}
