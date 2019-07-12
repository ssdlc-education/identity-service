package com.yahoo.identity.services.token;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface TokenCreate {

    @Nonnull
    TokenCreate setUsername(@Nonnull String username);

    @Nonnull
    TokenCreate setExpireTime(@Nonnull Instant expiryTime);

    @Nonnull
    Token create();
}
