package org.openapitools.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import com.yahoo.identity.services.storage.AccountConvert;
import com.yahoo.identity.services.token.TokenCreate;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.AccountApi;
import org.openapitools.model.Token;

import java.util.Map;

import javax.annotation.Nonnull;
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
        try {
            Session session = identity.getSessionService().newAnonymousSession();
            Account account = session.getAccount(username);

            AccountApi accountApi = new AccountApi();
            accountApi.setUsername(username);
            accountApi.setDescription(account.getDescription());

            return Response.status(Response.Status.OK).entity(accountApi).build();
        } catch (IdentityException e) {
            switch (e.getError()) {
                case ACCOUNT_NOT_FOUND:
                    return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }

    @Override
    public Response accountsmeGet(String cookie, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie);
            Account account = loggedInSession.getAccount();

            AccountApi accountApi = new AccountApi();
            accountApi.setUsername(account.getUsername());
            accountApi.setFirstName(account.getFirstName());
            accountApi.setLastName(account.getLastName());
            accountApi.setEmail(account.getEmail());
            accountApi.setDescription(account.getDescription());

            return Response.status(Response.Status.OK).entity(accountApi).build();

        } catch (IdentityException e) {
            switch (e.getError()) {
                case ACCOUNT_NOT_FOUND:
                    return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
                case INVALID_CREDENTIAL:
                    return Response.status(Response.Status.UNAUTHORIZED).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }

    @Override
    public Response accountsPost(AccountApi accountApi, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            Session session = identity.getSessionService().newAnonymousSession();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> accountMap = objectMapper.convertValue(accountApi, Map.class);
            Account account = new AccountConvert(accountMap);

            session.sessionAccountCreate(account);

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());
            String cookie = loggedInSession.getCredential().toString();

            return Response.status(Response.Status.CREATED).entity("The account is created successfully.")
                .header("Set-Cookie", "V=" + cookie).build();

        } catch (IdentityException e) {
            switch (e.getError()) {
                case INVALID_ARGUMENTS:
                    return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }

    @Override
    public Response accountsmePut(String cookie, String token, AccountApi accountApi, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        try {
            TokenCreate tokenCreate = identity.getTokenService().newTokenCreate();
            tokenCreate.setToken(token);
            if (accountApi.getEmails() == null && accountApi.getPassword() == null) {
                tokenCreate.setType(Token.TypeEnum.STANDARD);
            } else {
                tokenCreate.setType(Token.TypeEnum.CRITICAL);
            }
            tokenCreate.create();

            LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie);

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> accountMap = objectMapper.convertValue(accountApi, Map.class);
            Account account = new AccountConvert(accountMap);

            loggedInSession.sessionAccountUpdate(account);

            return Response.status(Response.Status.NO_CONTENT).entity("Successfully upate the account.").build();

        } catch (IdentityException e) {
            switch (e.getError()) {
                case ACCOUNT_NOT_FOUND:
                    return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
                case INVALID_ARGUMENTS:
                    return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
                case INVALID_CREDENTIAL:
                    return Response.status(Response.Status.UNAUTHORIZED).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }
}