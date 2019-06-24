package com.yahoo.identity.services.token;

import org.openapitools.model.Token.TypeEnum;

import javax.annotation.Nonnull;

public interface TokenCreate {

    @Nonnull
    TokenCreate setType(@Nonnull TypeEnum type);

    @Nonnull
    TokenCreate setToken(@Nonnull String tokenStr);

    @Nonnull
    Token create();
}
