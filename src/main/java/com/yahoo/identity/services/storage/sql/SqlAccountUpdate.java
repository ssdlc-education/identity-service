package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlAccountUpdate implements AccountUpdate {
    private final SqlSessionFactory sqlSessionFactory;
    private AccountModel account;

    public SqlAccountUpdate(@Nonnull SqlSessionFactory sqlSessionFactory, @Nonnull String id) {
        this.sqlSessionFactory = sqlSessionFactory;

        try(SqlSession session = sqlSessionFactory.openSession()){
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            this.account = mapper.getAccount(id);
        }catch (Exception e) {
            this.account = new AccountModel();
        }
    }

    @Override
    @Nonnull
    public AccountUpdate setUsername(@Nonnull String username) {
        account.setUsername(username);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setFirstName(@Nonnull String firstName) {
        account.setFirstName(firstName);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setLastName(@Nonnull String lastName) {
        account.setLastName(lastName);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setEmail(@Nonnull String email) {
        account.setEmail(email);
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
    public AccountUpdate setCreateTime(@Nonnull Instant createTime) {
        account.setCreateTs(createTime.toEpochMilli());
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
    public String update() throws IdentityException {
        /*
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.updateAccount(account);
            session.commit();
        }
        return account.getUsername();
         */
        return null;
    }
}