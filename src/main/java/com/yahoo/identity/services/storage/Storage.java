package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;

import javax.annotation.Nonnull;

public interface Storage {

    @Nonnull
    AccountCreate newAccountCreate();

    @Nonnull
    Account getAccount(@Nonnull String id);

    @Nonnull
    Account getPublicAccount(@Nonnull String id);

    @Nonnull
    Account getPublicAccount(@Nonnull String id);

    @Nonnull
    Account getPublicAccount(@Nonnull String id);

    @Nonnull
    AccountUpdate newAccountUpdate(@Nonnull String id);
}
