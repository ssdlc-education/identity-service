package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.key.KeyServiceImpl;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class CredentialServiceImpl implements CredentialService {

    private final KeyService keyService = new KeyServiceImpl();
    private final Credential credential = new CredentialImpl();

    @Override
    @Nonnull
    public Credential fromString(@Nonnull String credStr) {
        try {
            Algorithm algorithm =
                Algorithm.RSA256((RSAPublicKey) this.keyService.getPublicKey("cookie-public.pem", "RSA"),
                                 (RSAPrivateKey) this.keyService.getPrivateKey("cookie-private.pem", "RSA"));

            JWTVerifier verifier = JWT.require(algorithm)
                .acceptLeeway(1)   // 1 sec for nbf and iat
                .acceptExpiresAt(5)   // 5 secs for exp
                .build();

            DecodedJWT jwt = verifier.verify(credStr);

            this.credential.setSubject(jwt.getSubject());
            this.credential.setIssueTime(jwt.getIssuedAt().toInstant());
            this.credential.setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new BadRequestException("JWT verification does not succeed:" + e.toString());
        }
        return this.credential;
    }
}
