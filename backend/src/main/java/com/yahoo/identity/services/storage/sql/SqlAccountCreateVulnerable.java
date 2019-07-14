package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.system.SystemService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;


public class SqlAccountCreateVulnerable implements AccountCreate {

    private final SqlSessionFactory sqlSessionFactory;
    private final SystemService systemService;
    private final AccountModel account = new AccountModel();

    public SqlAccountCreateVulnerable(
        @Nonnull SqlSessionFactory sqlSessionFactory,
        @Nonnull SystemService systemService) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.systemService = systemService;
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
    public AccountCreate setPassword(@Nonnull String password) {
        account.setPasswordHash(DigestUtils.md5Hex(password));
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
        account.setCreateTs(systemService.currentTimeMillis());
        account.setUpdateTs(systemService.currentTimeMillis());
        account.setEmailVerified(false);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            try {
                mapper.insertAccount(account);
                session.commit();
            } catch (Exception e) {
                throw new IdentityException(IdentityError.INVALID_ARGUMENTS, "Account already exists.", e);
            }
        }
        return account.getUsername();
    }
}
