package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public interface SessionCreate {

    @Nonnull
    String getUsername();

    @Nonnull
    SessionCreate setUsername(@Nonnull String username);

    @Nonnull
    String getPassword();

    @Nonnull
    SessionCreate setPassword(@Nonnull String password);

    @Nonnull
    SessionCreate setCredential(@Nonnull String credStr);

    @Nonnull
    SessionCreate initCredential();

    @Nonnull
    String create();
}
