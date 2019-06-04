package com.yahoo.identity.services.storage;

import com.yahoo.identity.Account;
import com.yahoo.identity.AccountCreate;

import javax.annotation.Nonnull;

public interface Storage {
    @Nonnull
    AccountCreate newAccountCreate();

    @Nonnull
    Account getAccount(@Nonnull String id);
}
