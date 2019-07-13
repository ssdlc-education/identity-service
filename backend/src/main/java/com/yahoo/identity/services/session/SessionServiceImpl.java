package com.yahoo.identity.services.session;

import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.token.TokenService;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    private final Storage storage;
    private final KeyService keyService;
    private final TokenService tokenService;

    public SessionServiceImpl(
        @Nonnull Storage storage,
        @Nonnull KeyService keyService,
        @Nonnull TokenService tokenService) {
        this.storage = storage;
        this.keyService = keyService;
        this.tokenService = tokenService;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(storage, keyService, tokenService);
        loggedInSession.verifyPassword(username, password);

        return loggedInSession;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(storage, keyService, tokenService);
        loggedInSession.setCredential(credStr);

        return loggedInSession;
    }

    @Override
    @Nonnull
    public Session newAnonymousSession() {
        return new SessionImpl(this.storage);
    }
}
