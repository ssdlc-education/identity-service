package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;
import org.apache.commons.lang3.Validate;

import java.time.Instant;
import java.util.Date;

import javax.annotation.Nonnull;

public class TokenImplVulnerable implements Token {

    private final KeyService keyService;
    private final Instant issueTime;
    private final Instant expireTime;
    private final String subject;

    private TokenImplVulnerable(@Nonnull TokenImplVulnerable.Builder builder, @Nonnull KeyService keyService) {
        this.keyService = keyService;
        this.issueTime = builder.issueTime;
        this.expireTime = builder.expireTime;
        this.subject = builder.subject;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.keyService.getSecret("token.key"));
            return JWT.create()
                .withExpiresAt(Date.from(this.expireTime))
                .withIssuedAt(Date.from(this.issueTime))
                .withSubject(this.subject)
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }

    @Override
    public void validate() {
        boolean valid = this.expireTime.compareTo(Instant.now()) > 0;
        if (!valid) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "Token has been expired.");
        }
    }

    public static class Builder {

        private Instant issueTime;
        private Instant expireTime;
        private String subject;
        private KeyService keyService;

        @Nonnull
        public TokenImplVulnerable.Builder setIssueTime(@Nonnull Instant issueTime) {
            this.issueTime = issueTime;
            return this;
        }

        @Nonnull
        public TokenImplVulnerable.Builder setExpireTime(@Nonnull Instant expireTime) {
            this.expireTime = expireTime;
            return this;
        }

        @Nonnull
        public TokenImplVulnerable.Builder setSubject(@Nonnull String subject) {
            this.subject = subject;
            return this;
        }

        @Nonnull
        public TokenImplVulnerable.Builder setKeyService(@Nonnull KeyService keyService) {
            this.keyService = keyService;
            return this;
        }

        @Nonnull
        public TokenImplVulnerable build() {
            Validate.notNull(issueTime, "Issue time cannot be null");
            Validate.notNull(expireTime, "Expiration time cannot be null");
            Validate.notNull(subject, "Subject cannot be null");
            Validate.notNull(keyService, "Key service cannot be null");
            return new TokenImplVulnerable(this, keyService);
        }
    }
}
