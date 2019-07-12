package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.Account;

import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImplVulnerable extends AccountsApiService {

    private final Identity identity;

    public AccountsApiServiceImplVulnerable(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response getAccount(String username, SecurityContext securityContext) throws NotFoundException {
        try {
            Session session = identity.getSessionService().newAnonymousSession();
            com.yahoo.identity.services.account.Account account = session.getAccount(username);

            Account accountApi = new Account();
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
    public Response getOwnAccount(String cookie, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie);
            com.yahoo.identity.services.account.Account account = loggedInSession.getAccount();

            Account accountApi = new Account();
            accountApi.setUsername(account.getUsername());
            accountApi.setFirstName(account.getFirstName());
            accountApi.setLastName(account.getLastName());
            accountApi.setEmail(account.getEmail());
            accountApi.setDescription(account.getDescription());

            System.out.println("First name: " + account.getFirstName());
            System.out.println("Last name: " + account.getLastName());
            System.out.println("Email: " + account.getEmail());

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
    public Response createAccount(Account account, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        try {
            System.out.println("Cookie: " + securityContext.getAuthenticationScheme());

            Session session = identity.getSessionService().newAnonymousSession();

            AccountCreate accountCreate = session.sessionAccountCreate();

            accountCreate.setUsername(account.getUsername());
            accountCreate.setFirstName(account.getFirstName());
            accountCreate.setLastName(account.getLastName());
            accountCreate.setEmail(account.getEmail());
            accountCreate.setEmailStatus(emailStatus);
            accountCreate.setPassword(account.getPassword());
            accountCreate.setCreateTime(Instant.now());
            accountCreate.setUpdateTime(Instant.now());
            accountCreate.setDescription(account.getDescription());
            accountCreate.create();

            System.out.println("First name: " + account.getFirstName());
            System.out.println("Last name: " + account.getLastName());
            System.out.println("Email: " + account.getEmail());
            System.out.println("Password: " + account.getPassword());

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());

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
    public Response updateAccount(String token, Account account, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        try {
            // token is not validated here, makes it possible for CSRF attack

            System.out.println("Token: " + token);

            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithCredential(securityContext.getAuthenticationScheme());

            AccountUpdate accountUpdate = loggedInSession.sessionAccountUpdate();

            String email = account.getEmail();
            String password = account.getPassword();
            String description = account.getDescription();

            System.out.println("Email: " + email);
            System.out.println("Password: " + password);

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