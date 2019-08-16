// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.storage;

import javax.annotation.Nonnull;

public interface StorageFactory {

    @Nonnull
    Storage createStorage();
}
