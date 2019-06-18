package com.yahoo.identity.services.session;

import com.yahoo.identity.services.storage.sql.CredentialModel;

import javax.annotation.Nonnull;

public interface Session {
    @Nonnull
    CredentialModel getCredential();
}
