package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.CookieParser;
import org.openapitools.api.Cookies;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.Account;

import java.net.HttpCookie;
import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImplVulnerable extends AccountsApiService {

    private final Identity identity;
    private final CookieParser cookieParser;

    public AccountsApiServiceImplVulnerable(@Nonnull Identity identity, @Nonnull CookieParser cookieParser) {
        this.identity = identity;
        this.cookieParser = cookieParser;
    }

    @Override
    public Response getAccount(String username, SecurityContext securityContext) throws NotFoundException {
        Session session = identity.getSessionService().newAnonymousSession();
        com.yahoo.identity.services.account.Account account = session.getAccount(username);

        Account accountApi = new Account();
        accountApi.setUsername(username);
        accountApi.setDescription(account.getDescription());

        return Response.status(Response.Status.OK).entity(accountApi).build();
    }

    @Override
    public Response getOwnAccount(String cookie, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie);
        com.yahoo.identity.services.account.Account account = loggedInSession.getAccount();

        Account accountApi = new Account();
        accountApi.setUsername(account.getUsername());
        accountApi.setFirstName(account.getFirstName());
        accountApi.setLastName(account.getLastName());
        accountApi.setEmail(account.getEmail());
        accountApi.setDescription(account.getDescription());

        return Response.status(Response.Status.OK).entity(accountApi).build();
    }

    @Override
    public Response createAccount(Account account, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
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

        LoggedInSession
            loggedInSession =
            identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());

        String cookieStr = loggedInSession.getCredential().toString();
        NewCookie cookie = new NewCookie("V", cookieStr);

        return Response.status(Response.Status.CREATED).entity("The account is created successfully.")
            .cookie(cookie).build();
    }

    @Override
    public Response updateAccount(String token, String cookieStr, Account account, SecurityContext securityContext)
        throws NotFoundException {
        final boolean emailStatus = true;
        // token is not validated here, makes it possible for CSRF attack
        HttpCookie cookie = cookieParser.parse(cookieStr)
            .getFirstByName(Cookies.NAME_CREDENTIAL)
            .orElseThrow(() -> new IdentityException(
                IdentityError.INVALID_CREDENTIAL,
                "Missing cookie or invalid cookie header"));
        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithCredential(cookie.getValue());

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
    }
}