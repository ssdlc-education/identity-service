package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public class SessionServiceImpl implements SessionService {

    @Override
    @Nonnull
    public SessionCreate newSessionCreate() {
        return new SessionCreateImpl();
    }
}
