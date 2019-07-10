package com.yahoo.identity.services.storage;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.storage.sql.AccountMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class AccountImplVulnerable implements Account {

    private static final int ABUSE_MAX_TRIES = 5;
    private static final long ABUSE_MIN_BLOCK = 300;
    private static final long ABUSE_MAX_BLOCK = 7200;
    private static final long ABUSE_BLOCK_FACTOR = 2;
    private static final int ABUSE_RESET_ZERO = 0;
    private final AccountModel accountModel;
    private final SqlSessionFactory sqlSessionFactory;

    public AccountImplVulnerable(@Nonnull SqlSessionFactory sqlSessionFactory, @Nonnull AccountModel accountModel) {
        this.accountModel = accountModel;
        this.sqlSessionFactory = sqlSessionFactory;
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

    @Override
    public boolean verify(@Nonnull String password) {
        Instant blockUntil = Instant.ofEpochMilli(this.accountModel.getBlockUntilTs());
        int consecutiveFails = this.accountModel.getConsecutiveFails();
        long blockTimeLeft = Instant.now().until(blockUntil, SECONDS);

        if (blockTimeLeft > 0) {
            return false;
        }

        if (!this.accountModel.getPasswordHash().equals(DigestUtils.md5Hex(password))) {
            if (consecutiveFails >= ABUSE_MAX_TRIES) {
                long blockTime =
                    (long) (Math.pow(ABUSE_BLOCK_FACTOR, consecutiveFails - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                this.accountModel
                    .setBlockUntilTs(Instant.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)).toEpochMilli());
            } else {
                this.accountModel.setBlockUntilTs(Instant.now().toEpochMilli());
            }
            this.accountModel.setConsecutiveFails(consecutiveFails + 1);
            this.update();

            throw new NotAuthorizedException("Username and password are not matched!");
        }

        if (consecutiveFails > 0) {
            this.accountModel.setConsecutiveFails(ABUSE_RESET_ZERO);
            this.accountModel.setBlockUntilTs(Instant.now().toEpochMilli());
            this.update();
        }
        return true;
    }

    private void update() {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.updateAccount(this.accountModel);
            session.commit();
        } catch (Exception e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Sql Session failed to open: " + e.toString());
        }
    }

}
