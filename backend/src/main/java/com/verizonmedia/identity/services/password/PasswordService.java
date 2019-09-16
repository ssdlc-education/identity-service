// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.password;

import javax.annotation.Nonnull;

public interface PasswordService {
    @Nonnull
    String createPasswordHash(@Nonnull String password);

    boolean verifyPasswordHash(@Nonnull String password, @Nonnull String passwordHash);
}
