package com.verizonmedia.identity.services.password;

import javax.annotation.Nonnull;

public interface PasswordService {
    @Nonnull
    String createPasswordHash(@Nonnull String password);

    boolean verifyPasswordHash(@Nonnull String password, @Nonnull String passwordHash);
}
