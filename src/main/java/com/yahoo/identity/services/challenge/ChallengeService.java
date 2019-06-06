package com.yahoo.identity.services.challenge;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public interface ChallengeService {

    @Nonnull
    ChallengeSubmit newChallengeSubmit();

    @Nonnull
    ChallengeRedeem newChallengeRedeem();
}
