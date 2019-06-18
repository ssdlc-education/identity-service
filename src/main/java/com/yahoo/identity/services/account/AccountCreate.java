package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;
import java.time.Instant;

public interface AccountCreate {
    @Nonnull
    AccountCreate setFirstName(@Nonnull String firstName);

    @Nonnull
    AccountCreate setLastName(@Nonnull String lastName);

    @Nonnull
    AccountCreate setUsername(@Nonnull String username);

    @Nonnull
    AccountCreate setEmail(@Nonnull String email, @Nonnull Boolean verified);

    @Nonnull
    AccountCreate setPassword(@Nonnull String password);

    @Nonnull
    AccountCreate setCreateTime(@Nonnull Instant createTime);

    @Nonnull
    AccountCreate setUpdateTime(@Nonnull Instant updateTime);

    @Nonnull
    AccountCreate setDescription(@Nonnull String description);

    @Nonnull
    String create() throws IdentityException;
}
