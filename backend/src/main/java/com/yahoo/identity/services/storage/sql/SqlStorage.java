package com.yahoo.identity.services.storage.sql;

import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.storage.AccountModelUpdater;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.system.SystemService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

import javax.annotation.Nonnull;

public class SqlStorage implements Storage {

    private final SystemService systemService;
    private final SqlSessionFactory sqlSessionFactory;

    public SqlStorage(@Nonnull SystemService systemService) {
        this.systemService = systemService;
        this.sqlSessionFactory = createSessionFactory();
    }

    public void createAccount(@Nonnull AccountModel account) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            try {
                mapper.insertAccount(account);
                session.commit();
            } catch (Exception e) {
                // TODO Should correct distinguish the reason of the failure
                throw new IdentityException(IdentityError.INVALID_ARGUMENTS,
                                            "Account already exists", e);
            }
        }
    }

    @Nonnull
    @Override
    public AccountModel getAccount(@Nonnull String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            return mapper.getAccount(username);
        } catch (Exception e) {
            // TODO Should correct distinguish the reason of the failure
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Cannot retrieve user account.", e);
        }
    }

    @Override
    @Nonnull
    public AccountModel getAndUpdateAccount(@Nonnull String username,
                                            @Nonnull AccountModelUpdater updater) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            AccountModel accountModel = updater.update(mapper.getAccountForUpdate(username));
            mapper.updateAccount(accountModel);
            return accountModel;
        } catch (Exception e) {
            // TODO Should correct distinguish the reason of the failure
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Failed to update the account", e);
        }
    }

    @Nonnull
    @Override
    public AccountModel getPublicAccount(@Nonnull String username) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            return mapper.getPublicAccount(username);
        } catch (Exception e) {
            // TODO Should correct distinguish the reason of the failure
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR,
                                        "Cannot retrieve user account.", e);
        }
    }

    @Override
    public void updateAccount(@Nonnull AccountModel accountModel) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.updateAccount(accountModel);
            session.commit();
        } catch (Exception e) {
            throw new IdentityException(IdentityError.INTERNAL_SERVER_ERROR, "Failed to update account", e);
        }
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
}