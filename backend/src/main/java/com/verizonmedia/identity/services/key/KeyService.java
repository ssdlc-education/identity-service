package com.verizonmedia.identity.services.key;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.annotation.Nonnull;

public interface KeyService {

    @Nonnull
    byte[] getHMACKey(@Nonnull String name);

    @Nonnull
    PublicKey getPublicKey(@Nonnull String publicKeyName, @Nonnull String crpytoScheme);

    @Nonnull
    PrivateKey getPrivateKey(@Nonnull String privateKeyName, @Nonnull String crpytoScheme);
}
