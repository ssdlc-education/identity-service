package com.verizonmedia.identity;

import com.verizonmedia.identity.services.session.SessionService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Identity {

    private final SessionService sessionService;

    public Identity(@Nonnull SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Nonnull
    public SessionService getSessionService() {
        return this.sessionService;
    }
}
