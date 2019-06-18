package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import io.swagger.models.auth.In;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlAccountUpdate implements AccountUpdate {
    private final SqlSessionFactory sqlSessionFactory;
    private AccountModel account = new AccountModel();

    public SqlAccountUpdate(@Nonnull SqlSessionFactory sqlSessionFactory, @Nonnull String id) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.account.setUsername(id);
    }

    @Override
    @Nonnull
    public AccountUpdate setEmail(@Nonnull String email, @Nonnull Boolean verified) {
        account.setEmail(email, verified);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setPassword(@Nonnull String password) {
        account.setPassword(password);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setUpdateTime(@Nonnull Instant updateTime) {
        account.setUpdateTs(updateTime.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setDescription(@Nonnull String title) {
        account.setDescription(title);
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setBlockUntil(@Nonnull Instant blockUntil) {
        account.setBlockUntil(blockUntil.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setNthTrial(@Nonnull int nthTrial) {
        account.setNthTrial(nthTrial);
        return this;
    }

    @Nonnull
    @Override
    public String update() throws IdentityException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.updateAccount(account);
            session.commit();
        }
        return account.getUsername();
    }
}