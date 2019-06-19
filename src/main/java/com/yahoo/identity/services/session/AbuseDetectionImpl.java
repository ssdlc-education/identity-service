package com.yahoo.identity.services.session;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;

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
    public boolean abuseDetection(@Nonnull String username, @Nonnull String password) {
        try {
            Account accountGet = this.identity.getAccountService().getAccount(username);
            AccountUpdate accountUpdate = this.identity.getAccountService().newAccountUpdate(username);

            String passwordDb = accountGet.getPassword();
            Instant blockUntil = accountGet.getBlockUntil();
            int nthTrial = accountGet.getNthTrial();
            long blockTimeLeft = Instant.now().until(blockUntil, SECONDS);

            if (blockTimeLeft > 0) {
                return true;
            }

            if (!password.equals(passwordDb)) {
                if (nthTrial >= ABUSE_MAX_TRIES) {
                    long blockTime =
                            (long) (Math.pow(ABUSE_BLOCK_FACTOR, nthTrial - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                    accountUpdate.setNthTrial(nthTrial + 1);
                    accountUpdate.setBlockUntil(Instant.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)));
                } else {
                    accountUpdate.setNthTrial(nthTrial + 1);
                }
                accountUpdate.update();
                return true;
            }

            if (nthTrial > 0) {
                accountUpdate.setNthTrial(0);
                accountUpdate.setBlockUntil(Instant.now());
                accountUpdate.update();
            }
            return false;
        }catch(Exception e){
            throw new BadRequestException("Password abuse detection runtime error.");
        }
    }
}
