package org.openapitools.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import com.yahoo.identity.services.storage.AccountImpl;
import com.yahoo.identity.services.token.TokenCreate;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.AccountApi;
import org.openapitools.model.Token;

import java.util.Map;

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
        Session session = identity.getSessionService().newAnonymousSession();
        Account account = session.getAccount(username);

        AccountApi accountApi = new AccountApi();
        accountApi.setUsername(username);
        accountApi.setDescription(account.getDescription());

        return Response.ok().entity(accountApi).build();
    }

    @Override
    public Response accountsmeGet(String token, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(token);
            Account account = loggedInSession.getAccount();

            AccountApi accountApi = new AccountApi();
            accountApi.setUsername(account.getUsername());
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
    public Response accountsPost(AccountApi accountApi, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            Session session = identity.getSessionService().newAnonymousSession();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> accountMap = objectMapper.convertValue(accountApi, Map.class);
            Account account = new AccountImpl(accountMap);

            session.sessionAccountCreate(account);

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());
            String cookie = loggedInSession.getCredential().toString();

            ApiResponseMessage
                successMsg =
                new ApiResponseMessage(Response.Status.NO_CONTENT.getStatusCode(),
                                       "The account is created successfully.");
            return Response.status(Response.Status.NO_CONTENT).entity(successMsg)
                .header("Set-Cookie", cookie + ";HttpOnly").build();

        } catch (BadRequestException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.BAD_REQUEST.getStatusCode(), "Invalid request: " + e.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs: " + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }

    @Override
    public Response accountsmePut(String token, AccountApi accountApi, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        try {
            TokenCreate tokenCreate = identity.getTokenService().newTokenCreate();
            tokenCreate.setToken(token).setType(Token.TypeEnum.CRITICAL);
            tokenCreate.create();

            LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(token);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> accountMap = objectMapper.convertValue(accountApi, Map.class);
            Account account = new AccountImpl(accountMap);

            loggedInSession.sessionAccountUpdate(account);

            ApiResponseMessage
                successMsg =
                new ApiResponseMessage(Response.Status.NO_CONTENT.getStatusCode(), "Successfully upate the account.");
            return Response.ok().entity(successMsg).build();

        } catch (NotAuthorizedException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
                                       "Invalid cookie credential is used:" + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs:" + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }
}