package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.IdentityException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;
import java.time.Instant;

public class SqlAccount implements Account {
    private AccountModel account;

    public SqlAccount(@Nonnull SqlSessionFactory sqlSessionFactory, @Nonnull String id) throws IdentityException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            this.account = mapper.getAccount(id);
            session.commit();
        } catch (Exception e) {
            this.account = new AccountModel();
        }
    }

    @Override
    @Nonnull
    public String getId() {
        return this.account.getUsername();
    }

    @Override
    @Nonnull
    public Instant getCreateTime() {
        return Instant.ofEpochMilli(account.getCreateTs());
    }


    @Override
    @Nonnull
    public Instant getUpdateTime() {
        return Instant.ofEpochMilli(account.getUpdateTs());
    }

}
