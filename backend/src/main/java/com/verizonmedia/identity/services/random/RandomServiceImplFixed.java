package com.verizonmedia.identity.services.random;

import java.security.SecureRandom;

import javax.annotation.Nonnull;

public class RandomServiceImplFixed implements RandomService {

    private final SecureRandom random = new SecureRandom();

    @Override
    @Nonnull
    public byte[] getRandomBytes(int size) {
        byte[] buffer = new byte[size];
        random.nextBytes(buffer);
        return buffer;
    }
}
