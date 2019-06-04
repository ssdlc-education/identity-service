package com.vox;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface TrackCreate {

    TrackCreate setCreateTime(@Nonnull Instant createTime);

    TrackCreate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    TrackCreate setTitle(@Nonnull String title);

    @Nonnull
    String create() throws VoxException;
}
