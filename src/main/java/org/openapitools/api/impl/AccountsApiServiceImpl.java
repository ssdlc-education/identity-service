package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.SessionCreate;
import com.yahoo.identity.services.token.TokenCreate;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.AccountApi;
import org.openapitools.model.Token;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
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
        Account account = identity.getAccountService().getAccount(username);
        AccountApi accountApi = new AccountApi();
        accountApi.setUsername(username);
        accountApi.setFirstName(account.getFirstName());
        accountApi.setLastName(account.getLastName());
        accountApi.setDescription(account.getDescription());

        return Response.ok().entity(accountApi).build();
    }

    @Override
    public Response accountsmeGet(String token, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            SessionCreate sessionCreate = identity.getSessionService().newSessionCreate();
            sessionCreate.create();

            String username = sessionCreate.getUsername();

            Account account = identity.getAccountService().getAccount(username);
            AccountApi accountApi = new AccountApi();
            accountApi.setUsername(username);
            accountApi.setFirstName(account.getFirstName());
            accountApi.setLastName(account.getLastName());
            accountApi.setEmail(account.getEmail());
            accountApi.setDescription(account.getDescription());

            return Response.ok().entity(accountApi).build();

        } catch (NotAuthorizedException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
                                       "Invalid cookie credential is used: " + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs: " + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }

    @Override
    public Response accountsPost(AccountApi account, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            AccountCreate accountCreate = identity.getAccountService().newAccountCreate();
            accountCreate.setUsername(account.getUsername());
            accountCreate.setFirstName(account.getFirstName());
            accountCreate.setLastName(account.getLastName());
            accountCreate.setEmail(account.getEmail());
            accountCreate.setEmailStatus(emailStatus);

            accountCreate.setPassword(account.getPassword());
            accountCreate.setCreateTime(Instant.now());
            accountCreate.setUpdateTime(Instant.now());
            accountCreate.setBlockUntilTime(Instant.now());
            accountCreate.setDescription(account.getDescription());
            accountCreate.create();

            SessionCreate sessionCreate = identity.getSessionService().newSessionCreate();
            sessionCreate.setUsername(account.getUsername());
            sessionCreate.setPassword(account.getPassword());
            sessionCreate.initCredential();
            String token = sessionCreate.create();

            ApiResponseMessage successMsg = new ApiResponseMessage(204, "The account is created successfully.");
            return Response.ok().entity(successMsg).header("Set-Cookie", token).build();

        } catch (BadRequestException e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(400, "Invalid request: " + e.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs: " + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }

    @Override
    public Response accountsmePut(String token, AccountApi account, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        try {
            TokenCreate tokenCreate = identity.getTokenService().newTokenCreate();
            tokenCreate.setToken(token);
            tokenCreate.setType(Token.TypeEnum.CRITICAL);
            tokenCreate.create();

            AccountUpdate accountUpdate = identity.getAccountService().newAccountUpdate(account.getUsername());
            accountUpdate.setEmail(account.getEmail());
            accountUpdate.setEmailStatus(emailStatus);
            accountUpdate.setPassword(account.getPassword());
            accountUpdate.setDescription(account.getDescription());
            accountUpdate.setUpdateTime(Instant.now());
            accountUpdate.update();

            ApiResponseMessage successMsg = new ApiResponseMessage(204, "Successfully upate the account.");
            return Response.ok().entity(successMsg).build();

        } catch (NotAuthorizedException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(401, "Invalid cookie credential is used:" + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs:" + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }
}