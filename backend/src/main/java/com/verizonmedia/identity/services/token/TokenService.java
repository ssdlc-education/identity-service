// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface TokenService {

    @Nonnull
    TokenCreate newTokenCreate();

    @Nonnull
    Token newTokenFromString(@Nonnull String tokenStr);
}
