package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;

import javax.annotation.Nonnull;

public interface Session {

    @Nonnull
    Account getAccount(@Nonnull String username);

    @Nonnull
    AccountCreate sessionAccountCreate();
}
