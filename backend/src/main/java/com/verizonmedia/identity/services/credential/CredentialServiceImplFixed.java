package com.verizonmedia.identity.services.credential;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.system.SystemService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import javax.annotation.Nonnull;

public class CredentialServiceImplFixed extends CredentialServiceImpl {

    private static final String KEY_NAME = "identity-cookie";

    public CredentialServiceImplFixed(
        @Nonnull KeyService keyService,
        @Nonnull AccountService accountService,
        @Nonnull SystemService systemService) {
        super(keyService, accountService, systemService);
    }

    @Override
    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.RSA256((RSAPublicKey) keyService.getPublicKey(KEY_NAME, "RSA"),
                                (RSAPrivateKey) keyService.getPrivateKey(KEY_NAME, "RSA"));
    }

    @Nonnull
    @Override
    public Credential fromPassword(@Nonnull String username, @Nonnull String password) {
        accountService.verifyAccountPassword(username, password);
        Instant now = systemService.now();
        return new CredentialImplFixed.Builder()
            .setAlgorithm(algorithm)
            .setSubject(username)
            .setIssueTime(now)
            .setExpireTime(now.plusSeconds(EXPIRY_SEC))
            .build();
    }

    @Nonnull
    Credential buildCredentialFromJWT(@Nonnull DecodedJWT jwt) {
        return new CredentialImplFixed.Builder()
            .setAlgorithm(algorithm)
            .setSubject(jwt.getSubject())
            .setIssueTime(jwt.getIssuedAt().toInstant())
            .setExpireTime(jwt.getExpiresAt().toInstant())
            .build();
    }
}
