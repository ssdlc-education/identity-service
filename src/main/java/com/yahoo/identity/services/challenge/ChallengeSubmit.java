package com.yahoo.identity.services.challenge;

import javax.annotation.Nonnull;

public interface ChallengeSubmit {

    @Nonnull
    ChallengeSubmit setUsername(@Nonnull String username);

    @Nonnull
    ChallengeSubmit setEmail(@Nonnull String email);

    void submit();
}
