package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface SessionService {

    @Nonnull
    SessionCreate newSessionCreate();
}
