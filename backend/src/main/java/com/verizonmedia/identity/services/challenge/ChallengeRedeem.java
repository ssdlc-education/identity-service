package com.verizonmedia.identity.services.challenge;

import javax.annotation.Nonnull;

public interface ChallengeRedeem {

    @Nonnull
    ChallengeRedeem setUsername();

    @Nonnull
    ChallengeRedeem setEmail();

    @Nonnull
    ChallengeRedeem setAnswer();

    void redeem();
}
