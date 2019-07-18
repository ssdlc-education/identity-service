package com.verizonmedia.identity;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.account.AccountServiceImpl;
import com.verizonmedia.identity.services.credential.CredentialService;
import com.verizonmedia.identity.services.credential.CredentialServiceImpl;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.key.KeyServiceImpl;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.password.PasswordServiceImpl;
import com.verizonmedia.identity.services.session.SessionService;
import com.verizonmedia.identity.services.session.SessionServiceImpl;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.storage.sql.SqlStorage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;
import com.verizonmedia.identity.services.token.TokenServiceImpl;

import javax.annotation.Nonnull;

public class DefaultIdentityFactory implements IdentityFactory {

    @Nonnull
    @Override
    public Identity create() {
        SystemService systemService = new SystemService();
        Storage storage = new SqlStorage(systemService);
        KeyService keyService = new KeyServiceImpl();
        TokenService tokenService = new TokenServiceImpl(keyService, systemService);
        PasswordService passwordService = new PasswordServiceImpl();
        AccountService accountService = new AccountServiceImpl(storage, passwordService, systemService);
        CredentialService credentialService = new CredentialServiceImpl(keyService, accountService, systemService);

        SessionService sessionService = new SessionServiceImpl(
            tokenService,
            accountService,
            credentialService,
            systemService);

        return new Identity(sessionService);
    }
}
