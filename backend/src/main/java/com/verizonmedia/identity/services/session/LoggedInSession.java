// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.Account;
import com.verizonmedia.identity.services.account.AccountUpdate;
import com.verizonmedia.identity.services.credential.Credential;
import com.verizonmedia.identity.services.token.Token;
import com.verizonmedia.identity.services.token.TokenType;

import javax.annotation.Nonnull;

public interface LoggedInSession {

    @Nonnull
    Credential getCredential();

    @Nonnull
    Account getAccount();

    @Nonnull
    String getUsername();

    @Nonnull
    SessionAccountUpdate newAccountUpdate();

    @Nonnull
    Token createToken(@Nonnull TokenType tokenType);

    void verifyPassword(@Nonnull String password);
}
