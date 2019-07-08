package com.yahoo.identity.services.random;

import java.security.SecureRandom;

import javax.annotation.Nonnull;

public class RandomServiceImpl implements RandomService {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Nonnull
    public SecureRandom getRandom() {
        return secureRandom;
    }
}
