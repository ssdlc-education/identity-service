package com.verizonmedia.identity.services.random;

import javax.annotation.Nonnull;

public interface RandomService {

    @Nonnull
    byte[] getRandomBytes(int size);
}
