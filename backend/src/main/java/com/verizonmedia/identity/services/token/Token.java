// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.token;

import javax.annotation.Nonnull;

public interface Token {

    @Nonnull
    String toString();

    @Nonnull
    String getSubject();

    void validate();
}
