package com.yahoo.identity.services.token;

import com.yahoo.identity.services.credential.Credential;

import javax.annotation.Nonnull;

public interface TokenCreate {

    @Nonnull
    TokenCreate setType(@Nonnull TokenType type);

    @Nonnull
    TokenCreate setCredential(@Nonnull Credential credential);

    @Nonnull
    Token create();
}
