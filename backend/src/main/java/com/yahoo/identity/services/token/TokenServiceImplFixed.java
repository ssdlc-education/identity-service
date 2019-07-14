package com.yahoo.identity.services.token;

import com.auth0.jwt.algorithms.Algorithm;
import com.yahoo.identity.services.key.KeyService;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.annotation.Nonnull;

public class TokenServiceImplFixed extends TokenServiceImpl {

    private static final String KEY_NAME = "identity-token";

    public TokenServiceImplFixed(@Nonnull KeyService keyService) {
        super(keyService);
    }

    @Override
    @Nonnull
    Algorithm createAlgorithm(@Nonnull KeyService keyService) {
        return Algorithm.RSA256((RSAPublicKey) keyService.getPublicKey(KEY_NAME, "RSA"),
                                (RSAPrivateKey) keyService.getPrivateKey(KEY_NAME, "RSA"));
    }

}
