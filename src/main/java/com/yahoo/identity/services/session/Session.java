package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;

import javax.annotation.Nonnull;

public interface Session {

    @Nonnull
    Account getAccount(String username);
}
