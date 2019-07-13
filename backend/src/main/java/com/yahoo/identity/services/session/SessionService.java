package com.yahoo.identity.services.session;


import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface SessionService {

    @Nonnull
    LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password);

    @Nonnull
    LoggedInSession newSessionWithCredential(@Nonnull String credStr);

    @Nonnull
    Session newAnonymousSession();
}
