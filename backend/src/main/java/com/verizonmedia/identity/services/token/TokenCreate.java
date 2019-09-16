// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface TokenCreate {

    @Nonnull
    TokenCreate setUsername(@Nonnull String username);

    @Nonnull
    TokenCreate setIssueTime(@Nonnull Instant issueTime);

    @Nonnull
    TokenCreate setExpireTime(@Nonnull Instant expiryTime);

    @Nonnull
    Token create();
}
