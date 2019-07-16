package com.verizonmedia.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.Validate;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

public class CredentialImpl implements Credential {

    public static class Builder {
        private Instant issueTime;
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
        public Builder setSubject(@Nonnull String subject) {
            this.subject = subject;
            return this;
        }

        @Nonnull
        public CredentialImpl build() {
            Validate.notNull(issueTime, "Issue time is required");
            Validate.notNull(subject, "Subject is required");
            Validate.notNull(algorithm, "Algorithm is required");
            return new CredentialImpl(this);
        }
    }

    private final Algorithm algorithm;
    private final Instant issueTime;
    private final String subject;

    private CredentialImpl(@Nonnull Builder builder) {
        algorithm = builder.algorithm;
        issueTime = builder.issueTime;
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
        return Instant.MAX;
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
            .withIssuedAt(Date.from(getIssueTime()))
            .withSubject(getSubject())
            .sign(algorithm);
    }
}
