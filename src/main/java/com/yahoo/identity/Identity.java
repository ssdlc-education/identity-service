package com.yahoo.identity;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.session.SessionService;
import com.yahoo.identity.services.credential.CredentialService;
import com.yahoo.identity.services.token.TokenService;
import com.yahoo.identity.services.challenge.ChallengeService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Identity {

    private final AccountService accountService;
    private final SessionService sessionService;

    public Identity(@Nonnull AccountService accountService, @Nonnull SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @Nonnull
    public AccountService getAccountService() {
        return this.accountService;
    }

    @Nonnull
    public SessionService getSessionService() { return sessionService; }

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
