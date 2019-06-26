package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.storage.AccountModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;


public class SqlAccountCreateVulnerable implements AccountCreate {

    private final SqlSessionFactory sqlSessionFactory;
    private final AccountModel account = new AccountModel();

    public SqlAccountCreateVulnerable(@Nonnull SqlSessionFactory sqlSessionFactory) {
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
    public AccountCreate setEmail(@Nonnull String email) {
        account.setEmail(email);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setEmailStatus(@Nonnull boolean emailStatus) {
        account.setEmailStatus(emailStatus);
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setPassword(@Nonnull String password) {
        account.setPasswordSalt("");
        account.setPasswordHash(DigestUtils.md5Hex(password));
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
            try {
                mapper.insertAccount(account);
                session.commit();
            } catch (Exception e) {
                throw new BadRequestException("account already exists");
            }
        }
        return account.getUsername();
    }
}
