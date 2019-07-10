package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class CredentialImpl implements Credential {

    private final KeyService keyService;
    private Instant issueTime;
    private Instant expireTime;
    private String subject;
    private int status;

    public CredentialImpl(@Nonnull KeyService keyService) {
        this.keyService = keyService;
    }

    @Override
    @Nonnull
    public Instant getIssueTime() {
        return this.issueTime;
    }

    @Override
    public void setIssueTime(@Nonnull Instant issueTime) {
        this.issueTime = issueTime;
    }

    @Override
    @Nonnull
    public Instant getExpireTime() {
        return this.expireTime;
    }

    @Override
    public void setExpireTime(@Nonnull Instant expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    @Nonnull
    public String getSubject() {
        return this.subject;
    }

    @Override
    public void setSubject(@Nonnull String subject) {
        this.subject = subject;
    }

    @Override
    @Nonnull
    public int getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(boolean status) {
        this.status = status ? 1 : 0;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            Algorithm algorithm =
                Algorithm.RSA256((RSAPublicKey) this.keyService.getPublicKey("cookie-public.pem", "RSA"),
                                 (RSAPrivateKey) this.keyService.getPrivateKey("cookie-private.pem", "RSA"));

            String cookie = JWT.create()
                .withExpiresAt(Date.from(getExpireTime()))
                .withIssuedAt(Date.from(getIssueTime()))
                .withClaim("sta", getStatus())
                .withSubject(getSubject())
                .sign(algorithm);
            return cookie;

        } catch (JWTCreationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.");
        }
    }

    @Override
    public void validate() {
        if (getExpireTime().compareTo(Instant.now()) < 0) {
            throw new NotAuthorizedException("token is not valid.");
        }
    }
}
