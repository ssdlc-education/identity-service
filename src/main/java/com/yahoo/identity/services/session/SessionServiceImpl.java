package com.yahoo.identity.services.session;

import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    private final Storage storage;
    private final KeyService keyService;

    public SessionServiceImpl(@Nonnull Storage storage, @Nonnull KeyService keyService) {
        this.storage = storage;
        this.keyService = keyService;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(this.storage, this.keyService);
        loggedInSession.verifyPassword(username, password);

        return loggedInSession;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(this.storage, this.keyService);
        loggedInSession.setCredential(credStr);

        return loggedInSession;
    }

    @Override
    @Nonnull
    public Session newAnonymousSession() {
        return new SessionImpl(this.storage);
    }
}
