package com.vox;

import com.vox.services.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class Vox {

    private final Storage storage;

    public Vox(@Nonnull Storage storage) {
        this.storage = storage;
    }

    @Nonnull
    public TrackCreate newTrackCreate() {
        return storage.newTrackCreate();
    }
}
