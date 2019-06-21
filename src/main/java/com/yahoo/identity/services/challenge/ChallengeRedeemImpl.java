package com.yahoo.identity.services.challenge;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;

import javax.annotation.Nonnull;

public class ChallengeRedeemImpl implements ChallengeRedeem {

    private AccountService accountService;
    private String username;
    private String email;
    private String answer;

    public ChallengeRedeemImpl(@Nonnull Storage storage) {
        this.accountService = new SqlAccountService(storage);
    }

    @Override
    @Nonnull
    public ChallengeRedeem setUsername(@Nonnull String username) {
        this.username = username;
        return this;
    }

    @Override
    @Nonnull
    public ChallengeRedeem setEmail(@Nonnull String email) {
        this.email = email;
        return this;
    }

    @Override
    @Nonnull
    public ChallengeRedeem setAnswer(@Nonnull String answer) {
        this.answer = answer;
        return this;
    }

    @Override
    public void redeem() {

    }

}
