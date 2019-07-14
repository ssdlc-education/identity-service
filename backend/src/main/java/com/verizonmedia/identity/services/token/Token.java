package com.verizonmedia.identity.services.token;

import javax.annotation.Nonnull;

public interface Token {

    @Nonnull
    String toString();

    @Nonnull
    String getSubject();

    void validate();
}
