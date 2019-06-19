package com.yahoo.identity.services.storage;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.SessionCreate;

import javax.annotation.Nonnull;

public interface Storage {

    @Nonnull
    AccountCreate newAccountCreate();

    @Nonnull
    SessionCreate newSessionCreate();

    @Nonnull
    Account getAccount(@Nonnull String id);

    @Nonnull
    AccountUpdate newAccountUpdate(@Nonnull String id);
}
