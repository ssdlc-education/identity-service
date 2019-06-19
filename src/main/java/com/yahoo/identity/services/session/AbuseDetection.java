package com.yahoo.identity.services.session;

import javax.annotation.Nonnull;

public interface AbuseDetection {

    @Nonnull
    boolean abuseDetection(@Nonnull String username, @Nonnull String password);
}
