package com.yahoo.identity;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.session.SessionService;
import com.yahoo.identity.services.storage.sql.SqlAccountService;
import com.yahoo.identity.services.token.TokenService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.challenge.ChallengeService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Identity {

    private final AccountService accountService;

    public Identity(@Nonnull AccountService accountService) {
        this.accountService = accountService;
    }

    @Nonnull
    public AccountService getAccountService() {
        return accountService;
    }

    @Nonnull
    public SessionService getSessionService() {
        //TODO
        return null;
    }

    @Nonnull
    public TokenService getTokenService() {
        //TODO
        return null;
    }

    @Nonnull
    public ChallengeService getVerificationService() {
        //TODO
        return null;
    }
}
