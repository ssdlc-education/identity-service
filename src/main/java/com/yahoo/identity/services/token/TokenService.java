package com.yahoo.identity.services.token;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface TokenService {

    @Nonnull
    TokenCreate newTokenCreate();

    @Nonnull
    Token newTokenFromString(@Nonnull String tokenStr);
}
