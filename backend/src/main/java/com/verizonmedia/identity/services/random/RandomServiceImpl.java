// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.random;

import java.util.Random;

import javax.annotation.Nonnull;

public class RandomServiceImpl implements RandomService {

    private final Random random = new Random();

    @Override
    @Nonnull
    public byte[] getRandomBytes(int size) {
        byte[] buffer = new byte[size];
        random.nextBytes(buffer);
        return buffer;
    }
}
