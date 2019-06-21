package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public interface SessionCreate {

    @Nonnull
    SessionCreate setUsername(@Nonnull String username);

    @Nonnull
    SessionCreate setPassword(@Nonnull String password);

    @Nonnull
    SessionCreate setCredential(@Nonnull String credStr);

    @Nonnull
    SessionCreate initCredential();

    @Nonnull
    String create();
}
