package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.AccountApi;

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

            AccountCreate accountCreate = session.sessionAccountCreate();

            accountCreate.setUsername(accountApi.getUsername());
            accountCreate.setFirstName(accountApi.getFirstName());
            accountCreate.setLastName(accountApi.getLastName());
            accountCreate.setEmail(accountApi.getEmail());
            accountCreate.setEmailStatus(emailStatus);
            accountCreate.setPassword(accountApi.getPassword());
            accountCreate.setCreateTime(Instant.now());
            accountCreate.setUpdateTime(Instant.now());
            accountCreate.setDescription(accountApi.getDescription());
            accountCreate.create();

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithPassword(accountApi.getUsername(), accountApi.getPassword());

            String cookieStr = loggedInSession.getCredential().toString();
            NewCookie cookie = new NewCookie("V", cookieStr);

            return Response.status(Response.Status.CREATED).entity("The account is created successfully.")
                .cookie(cookie).build();

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
    public Response accountsmePut(String token, AccountApi accountApi, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        try {
            identity.getTokenService().newTokenFromString(token);

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithCredential(securityContext.getAuthenticationScheme());

            AccountUpdate accountUpdate = loggedInSession.sessionAccountUpdate();

            String email = accountApi.getEmail();
            String password = accountApi.getPassword();
            String description = accountApi.getDescription();

            if (email != null) {
                accountUpdate.setEmail(email);
            }
            if (password != null) {
                accountUpdate.setPassword(password);
            }
            if (description != null) {
                accountUpdate.setDescription(description);
            }

            accountUpdate.setEmailStatus(emailStatus);
            accountUpdate.setUpdateTime(Instant.now());
            accountUpdate.update();

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