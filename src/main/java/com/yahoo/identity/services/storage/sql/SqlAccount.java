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
            //System.out.println(this.account.getDescription());
            session.commit();
        } catch (Exception e) {
            this.account = new AccountModel();
        }
    }

    @Override
    @Nonnull
    public int getUid() {
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
    @Nonnull
    public String getPassword() {
        return this.account.getPassword();
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

    @Override
    @Nonnull
    public String getDescription() {
        return this.account.getDescription();
    }

}
