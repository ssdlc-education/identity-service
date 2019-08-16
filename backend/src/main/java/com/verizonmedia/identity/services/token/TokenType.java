// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import java.time.Duration;

import javax.annotation.Nonnull;

public enum TokenType {
    STANDARD(Duration.ofMillis(-1)),
    CRITICAL(Duration.ofMinutes(5));

    private Duration maxSessionAge;

    TokenType(@Nonnull Duration maxSessionAgeMs) {
        this.maxSessionAge = maxSessionAgeMs;
    }

    public boolean needRelogin(@Nonnull Duration sessionAge) {
        return !maxSessionAge.isNegative() && sessionAge.compareTo(maxSessionAge) > 0;
    }
}
