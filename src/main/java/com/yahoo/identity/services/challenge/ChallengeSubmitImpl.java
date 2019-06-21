package com.yahoo.identity.services.challenge;

import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountService;
import com.yahoo.identity.services.storage.Storage;
import com.yahoo.identity.services.storage.sql.SqlAccountService;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;

public class ChallengeSubmitImpl implements ChallengeSubmit {

    private AccountService accountService;
    private String username;
    private String email;

    public ChallengeSubmitImpl(@Nonnull Storage storage) {
        this.accountService = new SqlAccountService(storage);
    }

    @Override
    @Nonnull
    public ChallengeSubmit setUsername(@Nonnull String username) {
        this.username = username;
        return this;
    }

    @Override
    @Nonnull
    public ChallengeSubmit setEmail(@Nonnull String email) {
        this.email = email;
        return this;
    }

    @Override
    public void submit() {
        Account account = this.accountService.getAccount(username);
        if (account.isEmailVerified()) {
            throw new BadRequestException("Invalid request: email has been already verified.");
        }
    }

}
