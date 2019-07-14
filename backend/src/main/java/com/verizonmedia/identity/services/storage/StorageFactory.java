package com.verizonmedia.identity.services.storage;

import javax.annotation.Nonnull;

public interface StorageFactory {

    @Nonnull
    Storage createStorage();
}
