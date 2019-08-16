// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.key;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.annotation.Nonnull;
import javax.ws.rs.InternalServerErrorException;

public class KeyServiceImpl implements KeyService {

    @Override
    @Nonnull
    public byte[] getHMACKey(@Nonnull String secretKeyName) {
        try {
            return KeyServiceUtils.readHMACFromVault(secretKeyName);
        } catch (Exception e) {
            throw new InternalServerErrorException("Unknown error occurs when reading file: " + e.toString());
        }
    }

    @Override
    @Nonnull
    public PublicKey getPublicKey(@Nonnull String publicKeyName, @Nonnull String cryptoScheme) {
        try {
            return KeyServiceUtils.readPublicKeyFromVault(publicKeyName, cryptoScheme);
        } catch (Exception e) {
            throw new InternalServerErrorException("Unknown error occurs when reading file: " + e.toString());
        }
    }

    @Override
    @Nonnull
    public PrivateKey getPrivateKey(@Nonnull String privateKeyName, @Nonnull String cryptoScheme) {
        try {
            return KeyServiceUtils.readPrivateKeyFromVault(privateKeyName, cryptoScheme);
        } catch (Exception e) {
            throw new InternalServerErrorException("Unknown error occurs when reading file: " + e.toString());
        }
    }
}
