package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.credential.Credential;

import javax.annotation.Nonnull;

public interface LoggedInSession {

    @Nonnull
    String getUsername();

    @Nonnull
    Credential getCredential();

    @Nonnull
    Account getAccount();

    @Nonnull
    LoggedInSession sessionAccountUpdate(@Nonnull Account account);
}
