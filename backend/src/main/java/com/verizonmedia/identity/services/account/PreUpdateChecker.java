package com.verizonmedia.identity.services.account;

import com.verizonmedia.identity.services.token.TokenType;

import javax.annotation.Nonnull;

public interface PreUpdateChecker {
    void check(@Nonnull TokenType tokenType);
}
