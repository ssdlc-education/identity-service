package com.yahoo.identity;

import javax.annotation.Nonnull;

public interface IdentityFactory {

    @Nonnull
    Identity create();
}
