package com.yahoo.identity.services.credential;

import javax.annotation.Nonnull;

public interface CredentialService {

    @Nonnull
    Credential fromString(@Nonnull String credStr);

}
