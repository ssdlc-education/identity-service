package com.yahoo.identity.services.challenge;

import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;

public class ChallengeServiceImpl implements ChallengeService {

    private final Storage storage;

    public ChallengeServiceImpl(@Nonnull Storage storage) {
        this.storage = storage;
    }

    @Override
    @Nonnull
    public ChallengeSubmit newChallengeSubmit() {
        ChallengeSubmit challengeSubmit = new ChallengeSubmitImpl(this.storage);
        return challengeSubmit;
    }

    @Override
    @Nonnull
    public ChallengeRedeem newChallengeRedeem() {
        ChallengeRedeem challengeRedeem = new ChallengeRedeemImpl(this.storage);
        return challengeRedeem;
    }

}
