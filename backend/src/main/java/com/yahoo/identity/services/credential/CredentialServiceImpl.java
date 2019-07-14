package com.yahoo.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.system.SystemService;

import java.time.Instant;

import javax.annotation.Nonnull;

public class CredentialServiceImpl implements CredentialService {

    private static final String KEY_NAME = "identity-cookie";
    private static final int EXPIRY_SEC = 86400;
    private final AccountService accountService;
    private final SystemService systemService;
    private final Algorithm algorithm;

    public CredentialServiceImpl(
        @Nonnull KeyService keyService,
        @Nonnull AccountService accountService,
        @Nonnull SystemService systemService) {
        this.accountService = accountService;
        this.systemService = systemService;
        this.algorithm = createAlgorithm(keyService);
    }

    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.HMAC256(keyService.getHMACKey(KEY_NAME));
    }

    @Nonnull
    @Override
    public Credential fromPassword(@Nonnull String username, @Nonnull String password) {
        accountService.verifyAccountPassword(username, password);
        Instant now = systemService.now();
        return new CredentialImpl.Builder()
            .setAlgorithm(algorithm)
            .setSubject(username)
            .setIssueTime(now)
            .setExpireTime(now.plusSeconds(EXPIRY_SEC))
            .build();
    }

    @Override
    @Nonnull
    public Credential fromString(@Nonnull String credStr) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                .build();

            DecodedJWT jwt = verifier.verify(credStr);

            Credential credential = new CredentialImpl.Builder()
                .setAlgorithm(algorithm)
                .setSubject(jwt.getSubject())
                .setIssueTime(jwt.getIssuedAt().toInstant())
                .setExpireTime(jwt.getExpiresAt().toInstant())
                .build();
            credential.validate();
            return credential;

        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }
}
