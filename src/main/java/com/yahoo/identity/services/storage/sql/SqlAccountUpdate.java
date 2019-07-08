package com.yahoo.identity.services.storage.sql;

import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import com.kosprov.jargon2.api.Jargon2;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.storage.AccountModel;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import javax.annotation.Nonnull;

public class SqlAccountUpdate implements AccountUpdate {

    private final SqlSessionFactory sqlSessionFactory;
    private final SecureRandom secureRandom;
    private AccountModel account = new AccountModel();

    public SqlAccountUpdate(@Nonnull SqlSessionFactory sqlSessionFactory, @Nonnull RandomService randomService,
                            @Nonnull String username) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.secureRandom = randomService.getRandom();
        this.account.setUsername(username);
    }

    @Override
    @Nonnull
    public AccountUpdate setEmail(@Nonnull String email) {
        account.setEmail(escapeHtml4(email));
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setEmailStatus(@Nonnull boolean emailStatus) {
        account.setEmailStatus(emailStatus);
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setPassword(@Nonnull String password) {

        byte[] saltBytes = new byte[64];
        secureRandom.nextBytes(saltBytes);

        account.setPasswordSalt(Base64.getEncoder().encodeToString(saltBytes));

        Jargon2.Hasher hasher = jargon2Hasher();
        account.setPasswordHash(hasher.salt(saltBytes).password(password.getBytes()).encodedHash());
        return this;
    }

    @Override
    @Nonnull
    public AccountUpdate setUpdateTime(@Nonnull Instant updateTime) {
        account.setUpdateTs(updateTime.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setDescription(@Nonnull String title) {
        account.setDescription(escapeHtml4(title));
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setBlockUntilTime(@Nonnull Instant blockUntil) {
        account.setBlockUntilTs(blockUntil.toEpochMilli());
        return this;
    }

    @Nonnull
    @Override
    public AccountUpdate setConsecutiveFails(@Nonnull int consecutiveFails) {
        account.setConsecutiveFails(consecutiveFails);
        return this;
    }

    @Nonnull
    @Override
    public String update() throws IdentityException {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            AccountMapper mapper = session.getMapper(AccountMapper.class);
            mapper.updateAccount(account);
            session.commit();
        }
        return account.getUsername();
    }
}