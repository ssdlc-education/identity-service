package com.verizonmedia.identity.services.credential;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.system.SystemService;

import java.time.Instant;

import javax.annotation.Nonnull;

public class CredentialServiceImpl implements CredentialService {

    private static final String KEY_NAME = "identity-cookie";
    static final int EXPIRY_SEC = 86400;
    final AccountService accountService;
    final SystemService systemService;
    final Algorithm algorithm;

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
            .build();
    }

    @Override
    @Nonnull
    public Credential fromString(@Nonnull String credStr) {
        try {
            JWTVerifier verifier = createVerifier();
            DecodedJWT jwt = verifier.verify(credStr);
            return buildCredentialFromJWT(jwt);
        } catch (JWTVerificationException e) {
            throw new IdentityException(IdentityError.INVALID_CREDENTIAL, "JWT verification does not succeed.", e);
        }
    }

    @Nonnull
    private JWTVerifier createVerifier() {
        return JWT.require(algorithm)
            .build();
    }

    @Nonnull
    Credential buildCredentialFromJWT(@Nonnull DecodedJWT jwt) {
        return new CredentialImpl.Builder()
            .setAlgorithm(algorithm)
            .setSubject(jwt.getSubject())
            .setIssueTime(jwt.getIssuedAt().toInstant())
            .build();
    }
}
