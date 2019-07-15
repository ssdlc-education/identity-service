package com.verizonmedia.identity.services.storage;

import java.util.Optional;

import javax.annotation.Nonnull;

public interface AccountModelUpdater {

    @Nonnull
    Optional<AccountModel> update(@Nonnull AccountModel account);
}
