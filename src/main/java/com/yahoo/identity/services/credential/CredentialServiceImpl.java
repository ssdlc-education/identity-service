package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.key.KeyServiceImpl;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class CredentialServiceImpl implements CredentialService {

    private KeyService keyService = new KeyServiceImpl();
    private CredentialImpl credential = new CredentialImpl();

    @Override
    @Nonnull
    public Credential fromString(@Nonnull String credStr) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(keyService.getSecret());

            JWTVerifier verifier = JWT.require(algorithm)
                .acceptLeeway(1)   // 1 sec for nbf and iat
                .acceptExpiresAt(5)   // 5 secs for exp
                .build();

            DecodedJWT jwt = verifier.verify(credStr);

            this.credential.setSubject(jwt.getSubject());
            this.credential.setIssueTime(jwt.getIssuedAt().toInstant());
            this.credential.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new BadRequestException("JWT verification does not succeed.");
        }
        return this.credential;
    }
}
