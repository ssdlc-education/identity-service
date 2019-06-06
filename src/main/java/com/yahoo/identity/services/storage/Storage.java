package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;

import javax.annotation.Nonnull;

public interface Storage {
    @Nonnull
    AccountCreate newAccountCreate();

    @Nonnull
    Account getAccount(@Nonnull String id);
}
