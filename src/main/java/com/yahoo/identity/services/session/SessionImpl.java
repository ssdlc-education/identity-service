package com.yahoo.identity.services.session;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.inject.Inject;

public class SessionImpl implements Session {

    @Inject
    private AccountService accountService;

    public SessionImpl(@Nonnull Storage storage) {
        this.accountService = new SqlAccountService(storage);
    }

    @Override
    @Nonnull
    public Account getAccount(@Nonnull String username) {
        return this.accountService.getPublicAccount(username);
    }

    @Nonnull
    public SessionImpl sessionAccountCreate(@Nonnull Account account) {
        final boolean mockEmailStatus = true;

        AccountCreate accountCreate = this.accountService.newAccountCreate();
        accountCreate.setUsername(account.getUsername());
        accountCreate.setFirstName(account.getFirstName());
        accountCreate.setLastName(account.getLastName());
        accountCreate.setEmail(account.getEmail());
        accountCreate.setEmailStatus(mockEmailStatus);

        accountCreate.setPassword(account.getPassword());
        accountCreate.setCreateTime(Instant.now());
        accountCreate.setUpdateTime(Instant.now());
        accountCreate.setBlockUntilTime(Instant.now());
        accountCreate.setDescription(account.getDescription());
        accountCreate.create();

        return this;
    }

}
