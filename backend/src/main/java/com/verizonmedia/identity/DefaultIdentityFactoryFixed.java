// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package com.verizonmedia.identity;

import com.verizonmedia.identity.services.account.AccountService;
import com.verizonmedia.identity.services.account.AccountServiceImplFixed;
import com.verizonmedia.identity.services.credential.CredentialService;
import com.verizonmedia.identity.services.credential.CredentialServiceImplFixed;
import com.verizonmedia.identity.services.key.KeyService;
import com.verizonmedia.identity.services.key.KeyServiceImpl;
import com.verizonmedia.identity.services.password.PasswordService;
import com.verizonmedia.identity.services.password.PasswordServiceImplFixed;
import com.verizonmedia.identity.services.random.RandomService;
import com.verizonmedia.identity.services.random.RandomServiceImplFixed;
import com.verizonmedia.identity.services.session.SessionService;
import com.verizonmedia.identity.services.session.SessionServiceImplFixed;
import com.verizonmedia.identity.services.storage.Storage;
import com.verizonmedia.identity.services.storage.sql.SqlStorage;
import com.verizonmedia.identity.services.system.SystemService;
import com.verizonmedia.identity.services.token.TokenService;
import com.verizonmedia.identity.services.token.TokenServiceImplFixed;

import javax.annotation.Nonnull;

public class DefaultIdentityFactoryFixed implements IdentityFactory {

    @Nonnull
    @Override
    public Identity create() {
        SystemService systemService = new SystemService();
        Storage storage = new SqlStorage(systemService);
        RandomService randomService = new RandomServiceImplFixed();
        KeyService keyService = new KeyServiceImpl();
        TokenService tokenService = new TokenServiceImplFixed(keyService, systemService);
        PasswordService passwordService = new PasswordServiceImplFixed(randomService);
        AccountService
            accountService = new AccountServiceImplFixed(storage, passwordService, systemService);
        CredentialService credentialService = new CredentialServiceImplFixed(keyService, accountService, systemService);

        SessionService sessionService = new SessionServiceImplFixed(
            tokenService,
            accountService,
            credentialService,
            systemService);

        return new Identity(sessionService);
    }
}
