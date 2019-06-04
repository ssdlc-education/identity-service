package com.vox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VoxException extends RuntimeException {

    private final VoxError error;

    public VoxException(@Nonnull VoxError error, @Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public VoxException(@Nonnull VoxError error, @Nullable String message) {
        this(error, message, null);
    }

    @Nonnull
    public VoxError getError() {
        return error;
    }
}
