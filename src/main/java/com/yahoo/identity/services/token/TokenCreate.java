package com.yahoo.identity.services.token;

import javax.annotation.Nonnull;

public interface TokenCreate {

    @Nonnull
    TokenCreate setTokenType(@Nonnull TokenType type);

    @Nonnull
    void initToken(@Nonnull String username);

    @Nonnull
    Token create();
}
