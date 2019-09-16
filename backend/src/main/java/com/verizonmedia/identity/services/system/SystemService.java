// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.system;

import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

import javax.annotation.Nonnull;

/**
 * This service is used for accessing system resources.
 */
public class SystemService {

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Nonnull
    public Instant now() {
        return Instant.now();
    }

    @Nonnull
    public InputStream getResourceAsStream(@Nonnull String resource) throws IOException {
        return Resources.getResourceAsStream(resource);
    }
}
