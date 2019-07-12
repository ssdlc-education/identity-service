package com.yahoo.identity.services.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.key.KeyService;
import org.apache.commons.lang3.Validate;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.annotation.Nonnull;

public class TokenImpl implements Token {

    public static class Builder {
        private Instant issueTime;
        private Instant expireTime;
        private String subject;
        private KeyService keyService;

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
        public Builder setKeyService(@Nonnull KeyService keyService) {
            this.keyService = keyService;
            return this;
        }

        @Nonnull
        public TokenImpl build() {
            Validate.notNull(issueTime, "Issue time cannot be null");
            Validate.notNull(expireTime, "Expiration time cannot be null");
            Validate.notNull(subject, "Subject cannot be null");
            Validate.notNull(keyService, "Key service cannot be null");
            return new TokenImpl(this, keyService);
        }
    }

    private final KeyService keyService;
    private final Instant issueTime;
    private final Instant expireTime;
    private final String subject;

    private TokenImpl(@Nonnull Builder builder, @Nonnull KeyService keyService) {
        this.keyService = keyService;
        this.issueTime = builder.issueTime;
        this.expireTime = builder.expireTime;
        this.subject = builder.subject;
    }

    @Override
    @Nonnull
    public String toString() {
        try {
            Algorithm algorithm =
                Algorithm.RSA256((RSAPublicKey) this.keyService.getPublicKey("token-public.pem", "RSA"),
                                 (RSAPrivateKey) this.keyService.getPrivateKey("token-private.pem", "RSA"));
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
}
