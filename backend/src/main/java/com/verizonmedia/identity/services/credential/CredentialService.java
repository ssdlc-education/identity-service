package com.verizonmedia.identity.services.credential;

import javax.annotation.Nonnull;

public interface CredentialService {

    @Nonnull
    Credential fromPassword(@Nonnull String username, @Nonnull String password);

    @Nonnull
    Credential fromString(@Nonnull String credStr);
}
