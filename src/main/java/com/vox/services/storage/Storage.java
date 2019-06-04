package com.vox.services.storage;

import com.vox.Track;
import com.vox.TrackCreate;

import javax.annotation.Nonnull;

public interface Storage {
    @Nonnull
    TrackCreate newTrackCreate();

    @Nonnull
    Track getTrack(@Nonnull String id);
}
