package com.yahoo.identity;

public enum IdentityError {

    // General bad request error
    INVALID_ARGUMENTS(400001),

    ACCOUNT_NOT_FOUND(404001),

    INTERNAL_SERVER_ERROR(500000);

    private int code;

    IdentityError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
