package com.vox;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface Track {

    @Nonnull
    String getId();

    @Nonnull
    Instant getCreateTime();

    @Nonnull
    Instant getUpdateTime();

    @Nonnull
    String getTitle();
}
