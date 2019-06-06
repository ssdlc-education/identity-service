package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public interface SessionCreate {

    @Nonnull
    SessionCreate setUsername(@Nonnull String username);

    @Nonnull
    SessionCreate setPassword(@Nonnull String password);

    @Nonnull
    Session create();
}
