package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.IdentityException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.time.Instant;


public class SqlAccountCreate implements AccountCreate {

    private final SqlSessionFactory sqlSessionFactory;
    private final AccountModel account = new AccountModel();

    public SqlAccountCreate(@Nonnull SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    @Nonnull
    public AccountCreate setUsername(@Nonnull String username) {
        account.setUsername(username);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setFirstName(@Nonnull String firstName) {
        account.setFirstName(firstName);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setLastName(@Nonnull String lastName) {
        account.setLastName(lastName);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setEmail(@Nonnull String email, @Nonnull Boolean verified) {
        account.setEmail(email, verified);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setPassword(@Nonnull String password) {
        account.setPassword(password);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setCreateTime(@Nonnull Instant createTime) {
        account.setCreateTs(createTime.toEpochMilli());
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setUpdateTime(@Nonnull Instant updateTime) {
        account.setUpdateTs(updateTime.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public AccountCreate setDescription(@Nonnull String title) {
        account.setDescription(title);
        return this;
    }

    @Nonnull
    @Override
    public String create() throws IdentityException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.insertAccount(account);
            session.commit();
        }
        return account.getUsername();
    }
}
