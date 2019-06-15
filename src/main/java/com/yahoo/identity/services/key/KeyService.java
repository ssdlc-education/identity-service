package com.yahoo.identity.services.key;

import javax.annotation.Nonnull;

public interface KeyService {

    @Nonnull
    String getSecret();

    void setSecret(@Nonnull String secret);
}
