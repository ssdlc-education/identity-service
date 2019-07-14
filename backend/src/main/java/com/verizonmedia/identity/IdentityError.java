package com.verizonmedia.identity;

public enum IdentityError {

    // General bad request error
    INVALID_ARGUMENTS(400001),

    INVALID_PASSWORD(400002),

    INVALID_CREDENTIAL(401001),

    INVALID_TOKEN(401002),

    ACCOUNT_BLOCKED(403001),

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
