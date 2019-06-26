package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class SessionServiceImpl implements SessionService {

    private final Storage storage;

    public SessionServiceImpl(@Nonnull Storage storage) {
        this.storage = storage;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithPassword(@Nonnull String username, @Nonnull String password) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(this.storage);
        loggedInSession.setUsername(username);
        loggedInSession.setPassword(password);
        loggedInSession.initCredential();

        Account account = loggedInSession.getAccount();
        if (!account.verify(password)) {
            throw new NotAuthorizedException("Account is locked!");
        }
        loggedInSession.setVerified(account.getEmailStatus());

        return loggedInSession;
    }

    @Override
    @Nonnull
    public LoggedInSession newSessionWithCredential(@Nonnull String credStr) {
        LoggedInSessionImpl loggedInSession = new LoggedInSessionImpl(this.storage);
        loggedInSession.setCredential(credStr);

        return loggedInSession;
    }

    @Override
    @Nonnull
    public Session newAnonymousSession() {
        return new SessionImpl(this.storage);
    }
}
