// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.storage;

import java.util.Optional;

import javax.annotation.Nonnull;

public interface AccountModelUpdater {

    @Nonnull
    Optional<AccountModel> update(@Nonnull AccountModel account);
}
