// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.services.token.TokenType;

import javax.annotation.Nonnull;

public interface PreUpdateChecker {
    void check(@Nonnull TokenType tokenType);
}
