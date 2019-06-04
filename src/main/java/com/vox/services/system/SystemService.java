package com.vox.services.system;

import org.apache.ibatis.io.Resources;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

/**
 * This service is used for accessing system resources.
 */
public class SystemService {
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Nonnull
    public InputStream getResourceAsStream(@Nonnull String resource) throws IOException {
        return Resources.getResourceAsStream(resource);
    }
}
