package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.services.key.KeyServiceImpl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class CredentialImpl implements Credential {

    private Instant issueTime;
    private Instant expireTime;
    private String subject;
    private KeyServiceImpl keyServiceImpl = new KeyServiceImpl();

    @Override
    @Nonnull
    public Instant getIssueTime() {
        return this.issueTime;
    }

    public void setIssueTime(@Nonnull Instant issueTime) {
        this.issueTime = issueTime;
    }

    @Override
    @Nonnull
    public Instant getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(@Nonnull Instant expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    @Nonnull
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(@Nonnull String subject) {
        this.subject = subject;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(keyServiceImpl.getSecret());
            String token = JWT.create()
                .withExpiresAt(Date.from(expireTime))
                .withIssuedAt(Date.from(issueTime))
                .withSubject(subject)
                .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new BadRequestException("JWT creation does not succeed.");
        }
    }

    public String fromString(@Nonnull String credStr) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(keyServiceImpl.getSecret());

            JWTVerifier verifier = JWT.require(algorithm)
                .acceptLeeway(1)   // 1 sec for nbf and iat
                .acceptExpiresAt(5)   // 5 secs for exp
                .build();

            DecodedJWT jwt = verifier.verify(credStr);

            setSubject(jwt.getSubject());
            setIssueTime(jwt.getIssuedAt().toInstant());
            setExpireTime(jwt.getExpiresAt().toInstant());

        } catch (JWTVerificationException e) {
            throw new BadRequestException("JWT verification does not succeed.");
        }
        return getSubject();
    }
}
