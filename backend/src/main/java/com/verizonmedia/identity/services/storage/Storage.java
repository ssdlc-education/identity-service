package com.verizonmedia.identity.services.storage;

import javax.annotation.Nonnull;

public interface Storage {

    void createAccount(@Nonnull AccountModel account);

    @Nonnull
    AccountModel getAccount(@Nonnull String username);

    @Nonnull
    AccountModel getPublicAccount(@Nonnull String username);

    void updateAccount(@Nonnull AccountModel accountModel);

    void getAndUpdateAccount(@Nonnull String username,
                                     @Nonnull AccountModelUpdater updater);
}
