package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;
import org.openapitools.model.Token.TypeEnum;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class TokenCreateImpl implements TokenCreate {

    private KeyService keyService;
    private Token token;

    public TokenCreateImpl(@Nonnull KeyService keyService) {
        this.keyService = keyService;
        this.token = new TokenImpl(this.keyService);
    }

    @Override
    @Nonnull
    public TokenCreate setType(@Nonnull TypeEnum type) {
        switch (type) {
            case CRITICAL:
                this.token.setTokenType(TokenType.CRITICAL);
                break;
            case STANDARD:
                this.token.setTokenType(TokenType.STANDARD);
                break;
            default:
                throw new BadRequestException("Token type is not valid");
        }
        return this;
    }

    @Override
    @Nonnull
    public TokenCreate setToken(@Nonnull String tokenStr) {
        try {
            Algorithm algorithm =
                Algorithm.RSA256((RSAPublicKey) this.keyService.getPublicKey("token-public.pem", "RSA"),
                                 (RSAPrivateKey) this.keyService.getPrivateKey("token-private.pem", "RSA"));
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(tokenStr);

            this.token.setSubject(jwt.getSubject());
            this.token.setIssueTime(jwt.getIssuedAt().toInstant());
            this.token.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.");
        }
        return this;
    }

    @Override
    @Nonnull
    public void initToken(@Nonnull String username) {
        this.token.setSubject(username);
        this.token.setIssueTime(Instant.now());
        switch (token.getTokenType()) {
            case CRITICAL:
                this.token.setExpireTime(Instant.now().plus(5, ChronoUnit.MINUTES));
                break;
            case STANDARD:
                this.token.setExpireTime(Instant.now().plus(30, ChronoUnit.MINUTES));
                break;
        }
    }

    @Override
    @Nonnull
    public Token create() {
        this.token.validate();
        return this.token;
    }
}
