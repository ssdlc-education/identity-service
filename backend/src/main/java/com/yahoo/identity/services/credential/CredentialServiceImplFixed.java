package com.yahoo.identity.services.credential;

import com.auth0.jwt.algorithms.Algorithm;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.system.SystemService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

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
}
