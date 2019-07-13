package com.yahoo.identity.services.token;

import javax.annotation.Nonnull;

public interface Token {

    @Nonnull
    String toString();
    void validate();
}
