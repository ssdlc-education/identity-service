package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.credential.Credential;

import javax.annotation.Nonnull;

public interface LoggedInSession {

    @Nonnull
    Credential getCredential();

    @Nonnull
    Account getAccount();

    @Nonnull
    String getUsername();

    @Nonnull
    AccountUpdate sessionAccountUpdate(@Nonnull String username);
}
