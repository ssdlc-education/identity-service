// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.credential;

import javax.annotation.Nonnull;

public interface CredentialService {

    @Nonnull
    Credential fromPassword(@Nonnull String username, @Nonnull String password);

    @Nonnull
    Credential fromString(@Nonnull String credStr);
}
