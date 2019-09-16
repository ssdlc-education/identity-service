// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.verizonmedia.identity.Validate;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

public class CredentialImplFixed implements Credential {

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
        public CredentialImplFixed build() {
            Validate.notNull(issueTime, "Issue time is required");
            Validate.notNull(expireTime, "Expiry time is required");
            Validate.notNull(subject, "Subject is required");
            Validate.notNull(algorithm, "Algorithm is required");
            return new CredentialImplFixed(this);
        }
    }

    private final Algorithm algorithm;
    private final Instant issueTime;
    private final Instant expireTime;
    private final String subject;

    private CredentialImplFixed(@Nonnull CredentialImplFixed.Builder builder) {
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
}
