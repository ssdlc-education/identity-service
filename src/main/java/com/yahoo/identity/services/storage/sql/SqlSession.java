package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.session.Session;
import javax.annotation.Nonnull;

public class SqlSession implements Session {
    private SessionModel session;

    @Override
    @Nonnull
    public CredentialModel getCredential(){
        return session.getCredential();
    }
}
