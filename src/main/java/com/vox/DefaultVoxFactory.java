package com.vox;

import com.vox.services.event.DummyEventService;
import com.vox.services.event.EventService;
import com.vox.services.id.IDService;
import com.vox.services.id.UUIDService;
import com.vox.services.storage.Storage;
import com.vox.services.storage.event.EventNotifyingStorage;
import com.vox.services.storage.sql.SqlStorage;
import com.vox.services.system.SystemService;

import javax.annotation.Nonnull;

public class DefaultVoxFactory implements VoxFactory {

    @Nonnull
    @Override
    public Vox create() {
        SystemService systemService = new SystemService();
        IDService idService = new UUIDService();
        EventService eventService = new DummyEventService();
        Storage sqlStorage = new SqlStorage(idService, systemService);
        Storage eventStorage = new EventNotifyingStorage(sqlStorage, eventService);

        return new Vox(eventStorage);
    }
}
