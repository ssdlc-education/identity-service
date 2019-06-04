package com.yahoo.identity;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface Account {

    @Nonnull
    String getId();

    @Nonnull
    Instant getCreateTime();

    @Nonnull
    Instant getUpdateTime();
}
