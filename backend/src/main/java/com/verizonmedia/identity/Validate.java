// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity;

public class Validate {
    public static <T> T notNull(T object, String message, Object... values) {
        if (object == null) {
            throw new IdentityException(
                IdentityError.INVALID_ARGUMENTS,
                String.format(message, values));
        } else {
            return object;
        }
    }
}
