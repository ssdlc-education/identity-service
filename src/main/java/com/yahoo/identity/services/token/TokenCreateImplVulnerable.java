package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.key.KeyServiceImpl;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class TokenCreateImplVulnerable implements TokenCreate {

    private final KeyService keyService = new KeyServiceImpl();
    private Token token = new TokenImpl();

    @Override
    @Nonnull
    public TokenCreate setType(@Nonnull org.openapitools.model.Token.TypeEnum type) {
        switch (type) {
            case CRITICAL:
                token.setTokenType(TokenType.CRITICAL);
                break;
            case STANDARD:
                token.setTokenType(TokenType.STANDARD);
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
            Algorithm algorithm = Algorithm.HMAC256(keyService.getSecret("token"));

            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT jwt = verifier.verify(tokenStr);

            token.setSubject(jwt.getSubject());
            token.setIssueTime(jwt.getIssuedAt().toInstant());
            token.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new BadRequestException("JWT verification does not succeed.");
        }
        token.validate();
        return this;
    }

    @Override
    @Nonnull
    public Token create() {
        return this.token;
    }
}
