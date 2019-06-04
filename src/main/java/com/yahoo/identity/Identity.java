package com.yahoo.identity;

import com.yahoo.identity.services.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Identity {

    private final Storage storage;

    public Identity(@Nonnull Storage storage) {
        this.storage = storage;
    }

    @Nonnull
    public AccountCreate newAccountCreate() {
        return storage.newAccountCreate();
    }
}
