package com.yahoo.identity.services.random;

import java.util.Random;

import javax.annotation.Nonnull;

public class RandomServiceImplFixed implements RandomService {

    private final Random random = new Random();

    @Override
    @Nonnull
    public byte[] getRandomBytes(int size) {
        byte[] buffer = new byte[size];
        random.nextBytes(buffer);
        return buffer;
    }
}
