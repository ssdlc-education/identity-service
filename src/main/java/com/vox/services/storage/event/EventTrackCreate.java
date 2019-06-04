package com.vox.services.storage.event;

import com.vox.TrackCreate;
import com.vox.VoxException;
import com.vox.services.event.EventService;

import javax.annotation.Nonnull;
import java.time.Instant;

public class EventTrackCreate implements TrackCreate {

    private final TrackCreate delegate;
    private final EventService eventService;

    public EventTrackCreate(@Nonnull TrackCreate delegate, @Nonnull EventService eventService) {
        this.delegate = delegate;
        this.eventService = eventService;
    }

    public TrackCreate setCreateTime(@Nonnull Instant createTime) {
        delegate.setCreateTime(createTime);
        return this;
    }

    @Override
    public TrackCreate setUpdateTime(@Nonnull Instant updateTime) {
        delegate.setUpdateTime(updateTime);
        return this;
    }

    @Nonnull
    @Override
    public TrackCreate setTitle(@Nonnull String title) {
        delegate.setTitle(title);
        return this;
    }

    @Nonnull
    @Override
    public String create() throws VoxException {
        String id = delegate.create();
        eventService.sendTrackCreateEvent(id);
        return id;
    }
}
