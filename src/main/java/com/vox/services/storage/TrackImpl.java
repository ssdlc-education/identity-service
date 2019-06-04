package com.vox.services.storage;

import com.vox.Track;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.Map;

public class TrackImpl implements Track {

    private final Map<String, Object> data;

    public TrackImpl(@Nonnull Map<String, Object> data) {
        this.data = data;
    }

    @Nonnull
    @Override
    public String getId() {
        return data.get("id").toString();
    }

    @Nonnull
    @Override
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(Long.valueOf(data.get("create_ts").toString()));
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(Long.valueOf(data.get("update_ts").toString()));
    }

    @Nonnull
    @Override
    public String getTitle() {
        return data.get("title").toString();
    }
}
