package com.yahoo.identity;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface AccountCreate {

    AccountCreate setCreateTime(@Nonnull Instant createTime);

    AccountCreate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountCreate setTitle(@Nonnull String title);

    @Nonnull
    String create() throws IdentityException;
}
