package com.yahoo.identity.services.token;

import java.time.Instant;

import javax.annotation.Nonnull;

public interface Token {

    void setIssueTime(@Nonnull Instant issueTime);

    void setExpireTime(@Nonnull Instant expireTime);

    void setSubject(@Nonnull String subject);

    void setTokenType(@Nonnull TokenType tokenType);

    void validate();
}
