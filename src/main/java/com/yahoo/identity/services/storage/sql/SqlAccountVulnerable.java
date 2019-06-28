package com.yahoo.identity.services.storage.sql;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.storage.AccountModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;

public class SqlAccountVulnerable implements Account {

    private static final int ABUSE_MAX_TRIES = 5;
    private static final long ABUSE_MIN_BLOCK = 300;
    private static final long ABUSE_MAX_BLOCK = 7200;
    private static final long ABUSE_BLOCK_FACTOR = 2;
    private static final int ABUSE_RESET_ZERO = 0;
    private AccountModel account;
    private SqlSessionFactory sqlSessionFactory;

    public SqlAccountVulnerable(@Nonnull SqlSessionFactory sqlSessionFactory) throws IdentityException {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Nonnull
    public void getAccount(@Nonnull String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            this.account = mapper.getAccount(username);
            session.commit();
        } catch (Exception e) {
            this.account = new AccountModel();
        }
    }

    @Nonnull
    public void getPublicAccount(@Nonnull String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            this.account = mapper.getPublicAccount(username);
            session.commit();
        } catch (Exception e) {
            this.account = new AccountModel();
        }
    }

    @Override
    @Nonnull
    public String getUid() {
        return this.account.getUid();
    }

    @Override
    @Nonnull
    public String getUsername() {
        return this.account.getUsername();
    }

    @Override
    @Nonnull
    public String getFirstName() {
        return this.account.getFirstName();
    }

    @Override
    @Nonnull
    public String getLastName() {
        return this.account.getLastName();
    }

    @Override
    @Nonnull
    public String getEmail() {
        return this.account.getEmail();
    }

    @Override
    public boolean isEmailVerified() {
        return this.account.getEmailStatus();
    }

    @Override
    @Nonnull
    public String getPassword() {
        return this.account.getPasswordHash();
    }

    @Override
    @Nonnull
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(this.account.getCreateTs());
    }

    @Override
    @Nonnull
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(this.account.getUpdateTs());
    }

    @Override
    @Nonnull
    public String getDescription() {
        return this.account.getDescription();
    }

    @Override
    @Nonnull
    public Instant getBlockUntilTime() {
        return Instant.ofEpochMilli(this.account.getBlockUntilTs());
    }

    @Override
    public int getConsecutiveFails() {
        return this.account.getConsecutiveFails();
    }

    @Override
    public boolean verify(@Nonnull String password) {
        Instant blockUntil = this.getBlockUntilTime();
        int consecutiveFails = this.getConsecutiveFails();
        long blockTimeLeft = Instant.now().until(blockUntil, SECONDS);

        AccountUpdate accountUpdateVulnerable = new SqlAccountUpdateVulnerable(this.sqlSessionFactory, getUsername());

        if (blockTimeLeft > 0) {
            return false;
        }

        if (!this.account.getPasswordHash().equals(DigestUtils.md5Hex(password))) {
            if (consecutiveFails >= ABUSE_MAX_TRIES) {
                long blockTime =
                    (long) (Math.pow(ABUSE_BLOCK_FACTOR, consecutiveFails - ABUSE_MAX_TRIES) * ABUSE_MIN_BLOCK);
                accountUpdateVulnerable
                    .setBlockUntilTime(Instant.now().plusSeconds(Math.min(ABUSE_MAX_BLOCK, blockTime)));
            } else {
                accountUpdateVulnerable.setBlockUntilTime(Instant.now());
            }
            accountUpdateVulnerable.setConsecutiveFails(consecutiveFails + 1);
            accountUpdateVulnerable.update();
            throw new NotAuthorizedException("Username and password are not matched!");
        }

        if (consecutiveFails > 0) {
            accountUpdateVulnerable.setConsecutiveFails(ABUSE_RESET_ZERO);
            accountUpdateVulnerable.setBlockUntilTime(Instant.now());
            accountUpdateVulnerable.update();
        }
        return true;
    }
}

