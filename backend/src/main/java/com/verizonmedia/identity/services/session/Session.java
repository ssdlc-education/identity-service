// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.session;

import com.verizonmedia.identity.services.account.Account;
import com.verizonmedia.identity.services.account.AccountCreate;

import javax.annotation.Nonnull;

public interface Session {

    @Nonnull
    Account getAccount(@Nonnull String username);

    @Nonnull
    AccountCreate newAccountCreate();
}
