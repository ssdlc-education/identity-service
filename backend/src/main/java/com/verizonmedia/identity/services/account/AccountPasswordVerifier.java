// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity.services.account;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.verizonmedia.identity.IdentityError;
import com.verizonmedia.identity.IdentityException;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.storage.AccountModel;
import com.verizonmedia.identity.services.storage.AccountModelUpdater;
import com.verizonmedia.identity.services.system.SystemService;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

import javax.annotation.Nonnull;

public class AccountPasswordVerifier implements AccountModelUpdater {
    private static final Logger logger = LoggerFactory.getLogger(AccountPasswordVerifier.class);
    private static final int ABUSE_MAX_TRIES = 5;
    private static final long ABUSE_MIN_BLOCK = 300;
    private static final long ABUSE_MAX_BLOCK = 7200;
    private static final long ABUSE_BLOCK_FACTOR = 2;

    private final String password;
    private final SystemService systemService;
    private final PasswordService passwordService;
    private boolean verified = false;
    public AccountPasswordVerifier(@Nonnull String password,
                                   @Nonnull PasswordService passwordService,
                                   @Nonnull SystemService systemService) {
        this.password = password;
        this.passwordService = passwordService;
        this.systemService = systemService;
    }

    public boolean isVerified() {
        return verified;
    }

    @Nonnull
    @Override
    public Optional<AccountModel> update(@Nonnull AccountModel accountModel) {
        verified = false;
        AccountModel newAccountModel = new AccountModel();
        long blockUntilTs = accountModel.getBlockUntilTs();
        Instant blockUntil = Instant.ofEpochMilli(blockUntilTs);
        long blockTimeLeft = systemService.now().until(blockUntil, SECONDS);

        if (blockTimeLeft > 0) {
            logger.info("Account \"{}\" is trying password while it's blocked",
                        StringEscapeUtils.escapeJava(accountModel.getUsername()));
            throw new IdentityException(IdentityError.ACCOUNT_BLOCKED, "The account is blocked");
        }
        int consecutiveFails = accountModel.getConsecutiveFails();
        verified = passwordService.verifyPasswordHash(password, accountModel.getPasswordHash());

        if (!verified) {
            if (consecutiveFails >= ABUSE_MAX_TRIES) {
                long blockTime =
                    (long) (Math.pow(ABUSE_BLOCK_FACTOR, consecutiveFails - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                newAccountModel.setBlockUntilTs(
                    systemService.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)).toEpochMilli());
            }
            newAccountModel.setConsecutiveFails(consecutiveFails + 1);
        } else {
            if (consecutiveFails > 0) {
                newAccountModel.setConsecutiveFails(0);
                newAccountModel.setBlockUntilTs(0);
            }
        }
        return Optional.of(newAccountModel);
    }
}
