package com.yahoo.identity.services.random;

import java.security.SecureRandom;

import javax.annotation.Nonnull;

public interface RandomService {

    @Nonnull
    SecureRandom getRandom();

}
