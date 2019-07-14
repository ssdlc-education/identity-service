package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.credential.Credential;
import com.yahoo.identity.services.token.Token;

import javax.annotation.Nonnull;

public interface LoggedInSession {

    @Nonnull
    Credential getCredential();

    @Nonnull
    Account getAccount();

    @Nonnull
    String getUsername();

    @Nonnull
    AccountUpdate newAccountUpdate();

    @Nonnull
    Token createToken();

    void verifyPassword(@Nonnull String password);

    void validateTokenString(@Nonnull String tokenStr);
}
