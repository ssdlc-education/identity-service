package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.Validate;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

public class CredentialImpl implements Credential {

    public static class Builder {
        private Instant issueTime;
        private Instant expireTime;
        private String subject;
        private Algorithm algorithm;

        @Nonnull
        public Builder setAlgorithm(@Nonnull Algorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        @Nonnull
        public Builder setIssueTime(@Nonnull Instant issueTime) {
            this.issueTime = issueTime;
            return this;
        }

        @Nonnull
        public Builder setExpireTime(@Nonnull Instant expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        @Nonnull
        public Builder setSubject(@Nonnull String subject) {
            this.subject = subject;
            return this;
        }

        @Nonnull
        public CredentialImpl build() {
            Validate.notNull(issueTime, "Issue time is required");
            Validate.notNull(expireTime, "Expiry time is required");
            Validate.notNull(subject, "Subject is required");
            Validate.notNull(algorithm, "Algorithm is required");
            return new CredentialImpl(this);
        }
    }

    private final Algorithm algorithm;
    private final Instant issueTime;
    private final Instant expireTime;
    private final String subject;

    private CredentialImpl(@Nonnull Builder builder) {
        algorithm = builder.algorithm;
        issueTime = builder.issueTime;
        expireTime = builder.expireTime;
        subject = builder.subject;
    }

    @Override
    @Nonnull
    public Instant getIssueTime() {
        return this.issueTime;
    }

    @Override
    @Nonnull
    public Instant getExpireTime() {
        return this.expireTime;
    }

    @Override
    @Nonnull
    public String getSubject() {
        return this.subject;
    }

    @Override
    @Nonnull
    public String toString() {
        return JWT.create()
            .withExpiresAt(Date.from(getExpireTime()))
            .withIssuedAt(Date.from(getIssueTime()))
            .withSubject(getSubject())
            .sign(algorithm);
    }

    @Override
    public void validate() {
        if (getExpireTime().compareTo(Instant.now()) < 0) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "Token has been expired.");
        }
    }
}
