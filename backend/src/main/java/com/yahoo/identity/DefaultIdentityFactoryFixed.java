package com.yahoo.identity;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.account.AccountServiceImpl;
import com.yahoo.identity.services.credential.CredentialService;
import com.yahoo.identity.services.credential.CredentialServiceImplFixed;
import com.yahoo.identity.services.key.KeyService;
import com.yahoo.identity.services.key.KeyServiceImpl;
import com.yahoo.identity.services.password.PasswordService;
import com.yahoo.identity.services.password.PasswordServiceImplFixed;
import com.yahoo.identity.services.random.RandomService;
import com.yahoo.identity.services.random.RandomServiceImplFixed;
import com.yahoo.identity.services.session.SessionService;
import com.yahoo.identity.services.session.SessionServiceImpl;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlStorage;
import com.yahoo.identity.services.system.SystemService;
import com.yahoo.identity.services.token.TokenService;
import com.yahoo.identity.services.token.TokenServiceImplFixed;

import javax.annotation.Nonnull;

public class DefaultIdentityFactoryFixed implements IdentityFactory {

    @Nonnull
    @Override
    public Identity create() {
        SystemService systemService = new SystemService();
        Storage storage = new SqlStorage(systemService);
        RandomService randomService = new RandomServiceImplFixed();
        KeyService keyService = new KeyServiceImpl();
        PasswordService passwordService = new PasswordServiceImplFixed(randomService);
        AccountService accountService = new AccountServiceImpl(storage, passwordService, systemService);
        CredentialService credentialService = new CredentialServiceImplFixed(keyService, accountService, systemService);

        TokenService tokenService = new TokenServiceImplFixed(keyService);
        SessionService sessionService = new SessionServiceImpl(tokenService, accountService, credentialService);

        return new Identity(sessionService);
    }
}
