package com.yahoo.identity.services.session;

import com.yahoo.identity.services.credential.Credential;

import javax.annotation.Nonnull;

public interface Session {

    @Nonnull
    Credential getCredential();
}
