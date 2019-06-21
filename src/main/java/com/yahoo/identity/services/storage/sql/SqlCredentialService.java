package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.credential.CredentialService;

import javax.annotation.Nonnull;

public class SqlCredentialService implements CredentialService {

    @Override
    @Nonnull
    public Credential fromString(@Nonnull String credStr){
        return null;
    }
}
