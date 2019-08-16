// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.challenge;

import javax.annotation.Nonnull;

public interface ChallengeSubmit {

    @Nonnull
    ChallengeSubmit setUsername();

    @Nonnull
    ChallengeSubmit setEmail();

    void submit();
}
