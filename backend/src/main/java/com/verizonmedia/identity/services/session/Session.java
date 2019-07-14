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
