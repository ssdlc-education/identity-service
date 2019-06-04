package com.vox.services.id;

import com.vox.VoxException;

import javax.annotation.Nonnull;

public interface IDService {

    @Nonnull
    String createTrackId() throws VoxException;
}
