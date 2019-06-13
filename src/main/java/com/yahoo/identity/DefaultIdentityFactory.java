package com.yahoo.identity;

import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;
import com.yahoo.identity.services.storage.sql.SqlStorage;
import com.yahoo.identity.services.system.SystemService;

import javax.annotation.Nonnull;

public class DefaultIdentityFactory implements IdentityFactory {

    @Nonnull
    @Override
    public Identity create() {
        SystemService systemService = new SystemService();
        Storage sqlStorage = new SqlStorage(systemService);
        AccountService accountService = new SqlAccountService(sqlStorage);

        return new Identity(accountService);
    }
}
