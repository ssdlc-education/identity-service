// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.credential;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Credential {

    @Nonnull
    Instant getIssueTime();

    @Nonnull
    Instant getExpireTime();

    @Nonnull
    String getSubject();

    @Nonnull
    String toString();
}
