package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.session.Session;
import com.yahoo.identity.services.session.SessionService;
import com.yahoo.identity.services.session.SessionCreate;
import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class SqlSessionService implements SessionService {
    private final Storage storage;

    public SqlSessionService(@Nonnull Storage storage) { this.storage = storage; }

    @Override
    @Nonnull
    public SessionCreate newSessionCreate() { return this.storage.newSessionCreate(); }
}
