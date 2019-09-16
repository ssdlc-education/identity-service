// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.system.SystemService;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

public class TokenImplFixed implements Token {

    public static class Builder {
        private Instant issueTime;
        private Instant expireTime;
        private String subject;
        private Algorithm algorithm;
        private SystemService systemService;

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
        public Builder setAlgorithm(@Nonnull Algorithm algorithm) {
            this.algorithm = algorithm;
            return this;
        }

        @Nonnull
        public Builder setSystemService(@Nonnull SystemService systemService) {
            this.systemService = systemService;
            return this;
        }

        @Nonnull
        public TokenImplFixed build() {
            Validate.notNull(issueTime, "Issue time cannot be null");
            Validate.notNull(expireTime, "Expiration time cannot be null");
            Validate.notNull(subject, "Subject cannot be null");
            Validate.notNull(algorithm, "Algorithm cannot be null");
            Validate.notNull(systemService, "System service cannot be null");
            return new TokenImplFixed(this);
        }
    }

    private final Algorithm algorithm;
    private final Instant issueTime;
    private final Instant expireTime;
    private final String subject;
    private final SystemService systemService;

    private TokenImplFixed(@Nonnull Builder builder) {
        this.algorithm = builder.algorithm;
        this.issueTime = builder.issueTime;
        this.expireTime = builder.expireTime;
        this.subject = builder.subject;
        this.systemService = builder.systemService;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            return JWT.create()
                .withExpiresAt(Date.from(this.expireTime))
                .withIssuedAt(Date.from(this.issueTime))
                .withSubject(this.subject)
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "Fail to sign the token", e);
        }
    }

    @Nonnull
    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public void validate() {
        if (expireTime.compareTo(systemService.now()) < 0) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "Token has expired.");
        }
    }
}
