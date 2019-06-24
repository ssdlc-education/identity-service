package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public class SessionCreateImpl implements SessionCreate {

    private final SessionImpl session = new SessionImpl();

    @Override
    @Nonnull
    public SessionCreate setUsername(@Nonnull String username) {
        session.setUsername(username);
        return this;
    }

    @Override
    @Nonnull
    public String getUsername() {
        return session.getUsername();
    }

    @Override
    @Nonnull
    public String getPassword() {
        return session.getPassword();
    }

    @Override
    @Nonnull
    public SessionCreate setPassword(@Nonnull String password) {
        session.setPassword(password);
        return this;
    }

    @Override
    @Nonnull
    public SessionCreate initCredential() {
        session.initCredential();
        return this;
    }

    @Override
    @Nonnull
    public String create() {
        return this.session.getCredential().toString();
    }
}
