package com.verizonmedia.identity.services.storage;

import javax.annotation.Nonnull;

public interface AccountModelUpdater {

    @Nonnull
    AccountModel update(@Nonnull AccountModel account);
}
