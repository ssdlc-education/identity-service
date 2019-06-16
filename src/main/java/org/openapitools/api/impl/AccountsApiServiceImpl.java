package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import com.yahoo.identity.IdentityException;
import org.openapitools.model.Account;

import java.time.Instant;
import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImpl extends AccountsApiService {

    private final Identity identity;

    public AccountsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response accountsIdGet(String username, SecurityContext securityContext) throws NotFoundException {
        String uid = identity.getAccountService().getAccount(username).getUid();
        String firstName = identity.getAccountService().getAccount(username).getFirstName();
        String lastName = identity.getAccountService().getAccount(username).getLastName();
        String email = identity.getAccountService().getAccount(username).getEmail();
        String password = identity.getAccountService().getAccount(username).getPassword();
        Instant createTs = identity.getAccountService().getAccount(username).getCreateTime();
        Instant updateTs = identity.getAccountService().getAccount(username).getUpdateTime();
        String description = identity.getAccountService().getAccount(username).getDescription();

        String msg = email;
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, msg)).build();
    }

    @Override
    public Response accountsPost(Account account, SecurityContext securityContext) throws NotFoundException {
        try {
            AccountCreate accountCreate = identity.getAccountService().newAccountCreate();
            accountCreate.setUsername(account.getUsername());
            accountCreate.setFirstName(account.getFirstName());
            accountCreate.setLastName(account.getLastName());
            accountCreate.setEmail(account.getEmail());
            accountCreate.setPassword(account.getPassword());
            accountCreate.setCreateTime(account.getCreateTime().toInstant());
            accountCreate.setUpdateTime(account.getUpdateTime().toInstant());
            accountCreate.setDescription(account.getDescription());
            accountCreate.create();

            NewCookie cookie = new NewCookie("ButterCookie","123112131232");
            return Response.ok(new ApiResponseMessage(ApiResponseMessage.OK, "SET!")).cookie(cookie).build();

        } catch (IdentityException e) {
            return Response.ok(new ApiResponseMessage(ApiResponseMessage.OK, "NOT SET!")).build();
        }
    }

    @Override
    public Response accountsupdatePut(String token, Account account, SecurityContext securityContext) throws NotFoundException {
        AccountUpdate accountUpdate = identity.getAccountService().newAccountUpdate(account.getUsername());
        accountUpdate.setEmail(account.getEmail());
        accountUpdate.setPassword(account.getPassword());
        accountUpdate.setDescription(account.getDescription());
        accountUpdate.setUpdateTime(account.getUpdateTime().toInstant());
        accountUpdate.update();

        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "PUT!")).build();
    }
}