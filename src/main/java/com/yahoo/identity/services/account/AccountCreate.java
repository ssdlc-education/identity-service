package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface AccountCreate {

    @Nonnull
    AccountCreate setCreateTime(@Nonnull Instant createTime);

    @Nonnull
    AccountCreate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountCreate setDescription(@Nonnull String description);

    @Nonnull
    String create() throws IdentityException;
}
