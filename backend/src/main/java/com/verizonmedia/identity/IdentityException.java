// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IdentityException extends RuntimeException {

    private final IdentityError error;

    public IdentityException(@Nonnull IdentityError error, @Nullable String message, @Nullable Throwable cause) {
        super(message, cause);
        this.error = error;
    }

    public IdentityException(@Nonnull IdentityError error, @Nullable String message) {
        this(error, message, null);
    }

    @Nonnull
    public IdentityError getError() {
        return error;
    }
}
