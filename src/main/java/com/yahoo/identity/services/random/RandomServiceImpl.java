package com.yahoo.identity.services.random;

import java.security.SecureRandom;
import java.time.Instant;

import javax.annotation.Nonnull;

public class RandomServiceImpl implements RandomService {

    private final SecureRandom secureRandom = new SecureRandom();

    public RandomServiceImpl() {
        secureRandom.setSeed(Instant.now().toString().getBytes());
    }

    @Override
    @Nonnull
    public SecureRandom getRandom() {
        return secureRandom;
    }
}
