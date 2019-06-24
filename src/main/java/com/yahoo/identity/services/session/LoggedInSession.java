package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface LoggedInSession extends Session {

    @Nonnull
    String getUsername();

    @Nonnull
    Instant getIssueTime();

    @Nonnull
    Instant getExpiryTime();

    boolean isEmailVerified();

    @Nonnull
    String getCredential();

    @Nonnull
    Account getAccount();
}
