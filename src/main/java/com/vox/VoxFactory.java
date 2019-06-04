package com.vox;

import javax.annotation.Nonnull;

public interface VoxFactory {

    @Nonnull
    Vox create();
}
