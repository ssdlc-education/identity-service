package com.vox.services.storage.event;

import com.vox.Track;
import com.vox.TrackCreate;
import com.vox.services.event.EventService;
import com.vox.services.storage.Storage;

import javax.annotation.Nonnull;


public class EventNotifyingStorage implements Storage {

    private final Storage delegate;
    private final EventService eventService;

    public EventNotifyingStorage(@Nonnull Storage delegate, @Nonnull EventService eventService) {
        this.delegate = delegate;
        this.eventService = eventService;
    }

    @Nonnull
    @Override
    public TrackCreate newTrackCreate() {
        return new EventTrackCreate(delegate.newTrackCreate(), eventService);
    }

    @Nonnull
    @Override
    public Track getTrack(@Nonnull String id) {
        // TODO
        return null;
    }


}
