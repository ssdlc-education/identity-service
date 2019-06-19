package com.yahoo.identity.services.session;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.AccountService;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class AbuseDetectionImpl implements AbuseDetection {

    public static final int ABUSE_MAX_TRIES = 5;
    public static final long ABUSE_MIN_BLOCK = 300;
    public static final long ABUSE_MAX_BLOCK = 7200;
    public static final long ABUSE_BLOCK_FACTOR = 2;

    private final Identity identity;

    public AbuseDetectionImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    @Nonnull
    public Boolean abuseDetection(@Nonnull String username, @Nonnull String password) {
        try {
            AccountService accountService = identity.getAccountService();
            String passwordDb = accountService.getAccount(username).getPassword();
            Instant blockUntil = accountService.getAccount(username).getBlockUntil();
            int nthTrial = accountService.getAccount(username).getNthTrial();
            long blockTimeLeft = Instant.now().until(blockUntil, SECONDS);

            if (!password.equals(passwordDb)) {
                if (blockTimeLeft > 0 && nthTrial >= ABUSE_MAX_TRIES) {
                    long
                        blockTime =
                        (long) (Math.pow(ABUSE_BLOCK_FACTOR, nthTrial - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                    accountService.newAccountUpdate(username).setNthTrial(nthTrial + 1);
                    accountService.newAccountUpdate(username)
                        .setBlockUntil(Instant.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)));
                } else if (blockTimeLeft == 0 && nthTrial < ABUSE_MAX_TRIES) {
                    accountService.newAccountUpdate(username).setNthTrial(nthTrial + 1);
                }
                return true;
            }

            if (blockTimeLeft > 0 || nthTrial > 0) {
                accountService.newAccountUpdate(username).setNthTrial(0);
                accountService.newAccountUpdate(username).setBlockUntil(Instant.now());
            }
            return false;
        }catch(Exception e){
            throw new BadRequestException("Password abuse detection runtime error.");
        }
    }
}
