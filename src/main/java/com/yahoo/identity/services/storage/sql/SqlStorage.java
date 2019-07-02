package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.random.RandomServiceImpl;
import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class SqlStorage implements Storage {

    private final SystemService systemService;
    private final RandomService randomService;
    private final SqlSessionFactory sqlSessionFactory;

    public SqlStorage(@Nonnull SystemService systemService, @Nonnull RandomService randomService) {
        this.systemService = systemService;
        this.randomService = randomService;
        this.sqlSessionFactory = createSessionFactory();
    }

    @Nonnull
    @Override
    public AccountCreate newAccountCreate() {
        return new SqlAccountCreate(sqlSessionFactory, randomService);
    }

    @Nonnull
    @Override
    public Account getAccount(@Nonnull String username) {
        AccountModel accountModel;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            accountModel = mapper.getAccount(username);
            session.commit();
        } catch (Exception e) {
            throw new BadRequestException("cannot retrieve username");
        }
        AccountImpl accountImpl = new AccountImpl(accountModel);
        return accountImpl;
    }

    @Nonnull
    @Override
    public Account getPublicAccount(@Nonnull String username) {
        AccountModel accountModel;
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            accountModel = mapper.getPublicAccount(username);
            session.commit();
        } catch (Exception e) {
            throw new BadRequestException("cannot retrieve username");
        }
        AccountImpl accountImpl = new AccountImpl(accountModel);
        return accountImpl;
    }

    @Nonnull
    @Override
    public AccountUpdate newAccountUpdate(@Nonnull String username) {
        return new SqlAccountUpdate(sqlSessionFactory, randomService, username);
    }

    @Nonnull
    private SqlSessionFactory createSessionFactory() {
        try {
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            InputStream inputStream = systemService.getResourceAsStream("mybatis-config.xml");
            return builder.build(inputStream);
        } catch (Exception ex) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR, "Fail to create session factory", ex);
        }
    }
}
