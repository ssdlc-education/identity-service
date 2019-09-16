// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.random;

import javax.annotation.Nonnull;

public interface RandomService {

    @Nonnull
    byte[] getRandomBytes(int size);
}
