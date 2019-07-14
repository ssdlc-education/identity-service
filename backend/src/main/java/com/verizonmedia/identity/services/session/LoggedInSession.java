package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.Account;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.token.Token;

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
}
