package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
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
    public Account getAccount(@Nonnull String id) {
        return null;
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
