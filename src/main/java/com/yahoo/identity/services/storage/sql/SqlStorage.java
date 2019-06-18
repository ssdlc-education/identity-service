package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.session.Session;
import com.yahoo.identity.services.session.SessionCreate;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.Nonnull;
import java.io.InputStream;

public class SqlStorage implements Storage {

    private final SystemService systemService;
    private final SqlSessionFactory sqlSessionFactory;

    public SqlStorage(@Nonnull SystemService systemService) {
        this.systemService = systemService;
        this.sqlSessionFactory = createSessionFactory();
    }

    @Nonnull
    @Override
    public AccountCreate newAccountCreate() {
        return new SqlAccountCreate(sqlSessionFactory);
    }

    @Nonnull
    @Override
    public SessionCreate newSessionCreate() {
        return new SqlSessionCreate(sqlSessionFactory);
    }

    @Nonnull
    @Override
    public Account getAccount(@Nonnull String username) {
        return new SqlAccount(sqlSessionFactory, username);
    }

    @Nonnull
    @Override
    public AccountUpdate newAccountUpdate(@Nonnull String username) { return new SqlAccountUpdate(sqlSessionFactory, username); }

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
