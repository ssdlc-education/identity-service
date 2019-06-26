package com.yahoo.identity;

import com.yahoo.identity.services.challenge.ChallengeService;
import com.yahoo.identity.services.session.SessionService;
import com.yahoo.identity.services.token.TokenService;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Identity {

    private final SessionService sessionService;
    private final TokenService tokenService;

    public Identity(@Nonnull SessionService sessionService, @Nonnull TokenService tokenService) {
        this.sessionService = sessionService;
        this.tokenService = tokenService;
    }

    @Nonnull
    public SessionService getSessionService() {
        return this.sessionService;
    }

    @Nonnull
    public TokenService getTokenService() {
        return this.tokenService;
    }

    @Nonnull
    public ChallengeService getVerificationService() {
        //TODO
        return null;
    }
}
