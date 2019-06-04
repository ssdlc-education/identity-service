package com.vox.services.id;

import com.vox.VoxException;

import javax.annotation.Nonnull;
import java.util.UUID;

public class UUIDService implements IDService {
    @Nonnull
    @Override
    public String createTrackId() throws VoxException {
        return UUID.randomUUID().toString();
    }
}
