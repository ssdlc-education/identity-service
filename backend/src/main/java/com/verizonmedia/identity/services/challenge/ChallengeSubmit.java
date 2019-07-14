package com.verizonmedia.identity.services.challenge;

import javax.annotation.Nonnull;

public interface ChallengeSubmit {

    @Nonnull
    ChallengeSubmit setUsername();

    @Nonnull
    ChallengeSubmit setEmail();

    void submit();
}
