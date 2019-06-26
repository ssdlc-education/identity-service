package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.yahoo.identity.services.key.KeyServiceImpl;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;

public class CredentialImpl implements Credential {

    private Instant issueTime;
    private Instant expireTime;
    private String subject;
    private int status;

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
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            KeyServiceImpl keyServiceImpl = new KeyServiceImpl();
            Algorithm algorithm = Algorithm.HMAC256(keyServiceImpl.getSecret("cookie"));
            String token = JWT.create()
                .withExpiresAt(Date.from(getExpireTime()))
                .withIssuedAt(Date.from(getIssueTime()))
                .withClaim("sta", getStatus())
                .withSubject(getSubject())
                .sign(algorithm);
            return token;

        } catch (JWTCreationException exception) {
            throw new BadRequestException("JWT creation does not succeed.");
        }
    }

    @Override
    public void validate() {
        if (getExpireTime().compareTo(Instant.now()) < 0) {
            throw new NotAuthorizedException("token is not valid.");
        }
    }
}
