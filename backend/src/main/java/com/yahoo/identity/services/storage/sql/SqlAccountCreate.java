package com.yahoo.identity.services.storage.sql;

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import com.kosprov.jargon2.api.Jargon2;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.storage.AccountModel;
import com.yahoo.identity.services.system.SystemService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;


public class SqlAccountCreate implements AccountCreate {

    private final RandomService randomService;
    private final SqlSessionFactory sqlSessionFactory;
    private final SystemService systemService;
    private final AccountModel account = new AccountModel();


    public SqlAccountCreate(
        @Nonnull SqlSessionFactory sqlSessionFactory,
        @Nonnull RandomService randomService,
        @Nonnull SystemService systemService) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.randomService = randomService;
        this.systemService = systemService;
    }

    @Override
    @Nonnull
    public AccountCreate setUsername(@Nonnull String username) {
        account.setUsername(escapeHtml4(username));
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setFirstName(@Nonnull String firstName) {
        account.setFirstName(escapeHtml4(firstName));
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setLastName(@Nonnull String lastName) {
        account.setLastName(escapeHtml4(lastName));
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setEmail(@Nonnull String email) {
        account.setEmail(escapeHtml4(email));
        return this;
    }

    @Override
    @Nonnull
    public AccountCreate setPassword(@Nonnull String password) {
        byte[] saltBytes = new byte[64];
        this.randomService.getRandomBytes(saltBytes);

        Jargon2.Hasher hasher = jargon2Hasher();
        account.setPasswordHash(hasher.salt(saltBytes).password(password.getBytes(StandardCharsets.UTF_8)).encodedHash());
        return this;
    }

    @Nonnull
    @Override
    public AccountCreate setDescription(@Nonnull String title) {
        account.setDescription(escapeHtml4(title));
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
