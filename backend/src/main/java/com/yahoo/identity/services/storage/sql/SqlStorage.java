package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

import javax.annotation.Nonnull;

public class SqlStorage implements Storage {

    private final SystemService systemService;
    private final SqlSessionFactory sqlSessionFactory;
    private final RandomService randomService;
    private AccountImpl accountImpl;

    public SqlStorage(@Nonnull SystemService systemService, @Nonnull RandomService randomService) {
        this.systemService = systemService;
        this.sqlSessionFactory = createSessionFactory();
        this.randomService = randomService;
    }

    @Nonnull
    @Override
    public AccountCreate newAccountCreate() {
        return new SqlAccountCreate(sqlSessionFactory, randomService, systemService);
    }

    @Nonnull
    @Override
    public Account getAccount(@Nonnull String username) {
        AccountModel accountModel;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            accountModel = mapper.getAccount(username);
        } catch (Exception e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR, "Cannot retrieve user account.", e);
        }
        if (accountModel == null) {
            throw new IdentityException(IdentityError.ACCOUNT_NOT_FOUND,
                                        "Account \"" + StringEscapeUtils.escapeJava(username) + "\" not found");
        }
        this.accountImpl = new AccountImpl(sqlSessionFactory, accountModel);
        return accountImpl;
    }

    @Nonnull
    @Override
    public Account getPublicAccount(@Nonnull String username) {
        AccountModel accountModel;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            accountModel = mapper.getPublicAccount(username);
        } catch (Exception e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR, "Cannot retrieve user account.", e);
        }
        if (accountModel == null) {
            throw new IdentityException(IdentityError.ACCOUNT_NOT_FOUND,
                                        "Account \"" + StringEscapeUtils.escapeJava(username) + "\" not found");
        }
        this.accountImpl = new AccountImpl(sqlSessionFactory, accountModel);
        return accountImpl;
    }

    @Nonnull
    @Override
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return new SqlAccountUpdate(
            sqlSessionFactory,
            randomService,
            systemService,
            username);
    }

    @Nonnull
    private SqlSessionFactory createSessionFactory() {
        try {
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            InputStream inputStream = systemService.getResourceAsStream("mybatis-config.xml");
            return builder.build(inputStream);
        } catch (Exception e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR, "Fail to create session factory", e);
        }
    }

    public boolean verify(@Nonnull String password) {
        return this.accountImpl.verify(password);
    }
}