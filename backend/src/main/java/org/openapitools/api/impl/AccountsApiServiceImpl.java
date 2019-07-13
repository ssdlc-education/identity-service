package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import com.yahoo.identity.services.token.Token;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.CookieParser;
import org.openapitools.api.Cookies;
import org.openapitools.api.NotFoundException;

import java.net.HttpCookie;
import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImpl extends AccountsApiService {

    private final Identity identity;
    private final CookieParser cookieParser;

    public AccountsApiServiceImpl(@Nonnull Identity identity, @Nonnull CookieParser cookieParser) {
        this.identity = identity;
        this.cookieParser = cookieParser;
    }

    @Override
    public Response getAccount(@Nonnull String username, @Nonnull SecurityContext securityContext) throws NotFoundException {
        Session session = identity.getSessionService().newAnonymousSession();
        Account account = session.getAccount(username);

        org.openapitools.model.Account accountModel = new org.openapitools.model.Account();
        accountModel.setUsername(username);
        accountModel.setDescription(account.getDescription());

        return Response.status(Response.Status.OK).entity(accountModel).build();
    }

    @Override
    public Response getOwnAccount(String cookieStr, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        HttpCookie cookie = cookieParser.parse(cookieStr)
            .getFirstByName(Cookies.NAME_CREDENTIAL)
            .orElseThrow(() -> new IdentityException(
                IdentityError.INVALID_CREDENTIAL,
                "Missing cookie or invalid cookie header"));
        LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie.getValue());
        com.yahoo.identity.services.account.Account account = loggedInSession.getAccount();

        org.openapitools.model.Account accountModel = new org.openapitools.model.Account();
        accountModel.setUsername(account.getUsername());
        accountModel.setFirstName(account.getFirstName());
        accountModel.setLastName(account.getLastName());
        accountModel.setEmail(account.getEmail());
        accountModel.setDescription(account.getDescription());

        return Response.status(Response.Status.OK).entity(accountModel).build();
    }

    @Override
    public Response createAccount(org.openapitools.model.Account account, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        Session session = identity.getSessionService().newAnonymousSession();

        session.sessionAccountCreate()
            .setUsername(account.getUsername())
            .setFirstName(account.getFirstName())
            .setLastName(account.getLastName())
            .setEmail(account.getEmail())
            .setEmailStatus(emailStatus)
            .setPassword(account.getPassword())
            .setCreateTime(Instant.now())
            .setUpdateTime(Instant.now())
            .setDescription(account.getDescription())
            .create();

        LoggedInSession
            loggedInSession =
            identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());

        String cookieStr = loggedInSession.getCredential().toString();
        NewCookie cookie = new NewCookie("V", cookieStr);

        return Response.status(Response.Status.CREATED)
            .entity("The account is created successfully.")
            .cookie(cookie)
            .build();
    }

    @Override
    public Response updateAccount(String token, String cookieStr, org.openapitools.model.Account account, SecurityContext securityContext)
        throws NotFoundException {
        HttpCookie cookie = cookieParser.parse(cookieStr)
            .getFirstByName(Cookies.NAME_CREDENTIAL)
            .orElseThrow(() -> new IdentityException(
                IdentityError.INVALID_CREDENTIAL,
                "Missing cookie or invalid cookie header"));

        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithCredential(cookie.getValue());
        loggedInSession.validateTokenString(token);

        AccountUpdate accountUpdate = loggedInSession.sessionAccountUpdate();

        String email = account.getEmail();
        String password = account.getPassword();
        String description = account.getDescription();

        if (email != null) {
            accountUpdate.setEmail(email);
        }
        if (password != null) {
            accountUpdate.setPassword(password);
        }
        if (description != null) {
            accountUpdate.setDescription(description);
        }
        accountUpdate.update();

        return Response.status(Response.Status.NO_CONTENT).entity("Successfully upate the account.").build();
    }
}