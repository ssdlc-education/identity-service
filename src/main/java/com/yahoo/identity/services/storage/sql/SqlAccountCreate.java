package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.AccountCreate;
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
    public AccountCreate setTitle(@Nonnull String title) {
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
