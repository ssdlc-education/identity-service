package com.yahoo.identity.services.account;

import com.yahoo.identity.IdentityException;

import javax.annotation.Nonnull;

public interface AccountUpdate {

    @Nonnull
    String update() throws IdentityException;
}