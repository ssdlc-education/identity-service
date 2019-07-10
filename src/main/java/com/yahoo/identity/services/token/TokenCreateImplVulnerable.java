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

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.annotation.Nonnull;

public class TokenCreateImplVulnerable implements TokenCreate {

    private final KeyService keyService;
    private Token token;

    public TokenCreateImplVulnerable(@Nonnull KeyService keyService) {
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
                throw new IdentityException(IdentityError.INVALID_ARGUMENTS, "Unsupported token type.");
        }
        return this;
    }

    @Override
    @Nonnull
    public TokenCreate setToken(@Nonnull String tokenStr) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.keyService.getSecret("token.key"));

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(tokenStr);

            this.token.setSubject(jwt.getSubject());
            this.token.setIssueTime(jwt.getIssuedAt().toInstant());
            this.token.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
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
