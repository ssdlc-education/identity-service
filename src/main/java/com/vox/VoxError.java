package com.vox;

public enum VoxError {

    // General bad request error
    INVALID_ARGUMENTS(400001),

    TRACK_NOT_FOUND(404001),

    PLAYLIST_NOT_FOUND(404001),

    INTERNAL_SERVER_ERROR(500000);

    private int code;

    VoxError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
