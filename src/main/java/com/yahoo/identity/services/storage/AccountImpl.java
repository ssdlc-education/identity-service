package com.yahoo.identity.services.storage;

import static com.kosprov.jargon2.api.Jargon2.jargon2Verifier;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.kosprov.jargon2.api.Jargon2;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.random.RandomServiceImpl;
import com.yahoo.identity.services.storage.sql.SqlAccountUpdate;

import java.time.Instant;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class AccountImpl implements Account {
    private final AccountModel accountModel;

    public AccountImpl(@Nonnull AccountModel accountModel) {
        this.accountModel = accountModel;
    }

    @Nonnull
    @Override
    public String getUid() {
        return this.accountModel.getUid();
    }

    @Nonnull
    @Override
    public String getUsername() {
        return this.accountModel.getUsername();
    }

    @Nonnull
    @Override
    public String getFirstName() {
        return this.accountModel.getFirstName();
    }

    @Nonnull
    @Override
    public String getLastName() {
        return this.accountModel.getLastName();
    }

    @Nonnull
    @Override
    public String getEmail() {
        return this.accountModel.getEmail();
    }

    @Override
    public boolean isEmailVerified() {
        return this.accountModel.getEmailStatus();
    }

    @Nonnull
    @Override
    public String getPassword() {
        return this.accountModel.getPasswordHash();
    }

    @Nonnull
    @Override
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(this.accountModel.getCreateTs());
    }

    @Nonnull
    @Override
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(this.accountModel.getUpdateTs());
    }


    @Nonnull
    @Override
    public String getDescription() {
        return this.accountModel.getDescription();
    }


    public boolean verify(@Nonnull String password) {
        Instant blockUntil = Instant.ofEpochMilli(this.accountModel.getBlockUntilTs());
        int consecutiveFails = this.accountModel.getConsecutiveFails();
        long blockTimeLeft = Instant.now().until(blockUntil, SECONDS);

        RandomService randomService = new RandomServiceImpl();
        AccountUpdate accountUpdate = new SqlAccountUpdate(this.sqlSessionFactory, randomService, getUsername());

        if (blockTimeLeft > 0) {
            return false;
        }

        Jargon2.Verifier verifier = jargon2Verifier();
        boolean isVerified = verifier
            .salt(this.account.getPasswordSalt().getBytes())
            .hash(this.account.getPasswordHash())
            .password(password.getBytes())
            .verifyEncoded();

        if (!isVerified) {
            if (consecutiveFails >= ABUSE_MAX_TRIES) {
                long blockTime =
                    (long) (Math.pow(ABUSE_BLOCK_FACTOR, consecutiveFails - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                accountUpdate.setBlockUntilTime(Instant.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)));
            } else {
                accountUpdate.setBlockUntilTime(Instant.now());
            }
            accountUpdate.setConsecutiveFails(consecutiveFails + 1);
            accountUpdate.update();
            throw new NotAuthorizedException("Username and password are not matched!");
        }

        if (consecutiveFails > 0) {
            accountUpdate.setConsecutiveFails(ABUSE_RESET_ZERO);
            accountUpdate.setBlockUntilTime(Instant.now());
            accountUpdate.update();
        }
        return true;
    }

//    @Override
//    @Nonnull
//    public boolean verify(@Nonnull String password) {
//        return false;
//    }

}
