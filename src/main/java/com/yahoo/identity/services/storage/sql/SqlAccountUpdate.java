package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Nonnull;

public class SqlAccountUpdate implements AccountUpdate {
    private final SqlSessionFactory sqlSessionFactory;

    public SqlAccountUpdate(@Nonnull SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Nonnull
    @Override
    public String update() throws IdentityException {
        return null;
    }
}
