// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.password;

import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;

public class PasswordServiceImpl implements PasswordService {

    @Nonnull
    @Override
    public String createPasswordHash(@Nonnull String password) {
        return DigestUtils.md5Hex(password);
    }

    @Override
    public boolean verifyPasswordHash(@Nonnull String password, @Nonnull String passwordHash) {
        String expectedHash = createPasswordHash(password);
        return expectedHash.equals(passwordHash);
    }
}
