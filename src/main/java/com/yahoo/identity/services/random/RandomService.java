package com.yahoo.identity.services.random;

import javax.annotation.Nonnull;

public interface RandomService {

    @Nonnull
    byte[] getRandomBytes(@Nonnull byte[] buffer);
}
