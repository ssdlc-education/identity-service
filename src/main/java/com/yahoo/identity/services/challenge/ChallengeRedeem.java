package com.yahoo.identity.services.challenge;

import javax.annotation.Nonnull;

public interface ChallengeRedeem {

    @Nonnull
    ChallengeRedeem setUsername(@Nonnull String username);

    @Nonnull
    ChallengeRedeem setEmail(@Nonnull String email);

    @Nonnull
    ChallengeRedeem setAnswer(@Nonnull String answer);

    void redeem();
}
