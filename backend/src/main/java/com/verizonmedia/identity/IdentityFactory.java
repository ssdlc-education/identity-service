// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity;

import javax.annotation.Nonnull;

public interface IdentityFactory {

    @Nonnull
    Identity create();
}
