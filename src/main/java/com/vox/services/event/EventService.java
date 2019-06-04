package com.vox.services.event;

import javax.annotation.Nonnull;

public interface EventService {
    void sendTrackCreateEvent(@Nonnull String trackId);
}
