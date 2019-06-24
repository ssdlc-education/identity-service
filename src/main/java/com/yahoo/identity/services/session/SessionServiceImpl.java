package com.yahoo.identity.services.session;

import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    public SessionServiceImpl(@Nonnull Storage storage) {

    }

    @Nonnull
    @Override
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        return null;
    }

    @Nonnull
    @Override
    public LoggedInSession newSessionWithCredential(@Nonnull String credential) {
        return null;
    }

    @Nonnull
    @Override
    public AnonymousSession newAnonymousSession() {
        return null;
    }
}
