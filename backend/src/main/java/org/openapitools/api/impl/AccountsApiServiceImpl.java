package org.openapitools.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.Session;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.CookieParser;
import org.openapitools.api.Cookies;
import org.openapitools.api.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImpl extends AccountsApiService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsApiServiceImpl.class);

    private final Identity identity;
    private final CookieParser cookieParser;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        return Response.status(Response.Status.OK)
            .entity(accountModel)
            .build();
    }

    @Override
    public Response getOwnAccount(String cookieStr, SecurityContext securityContext) throws NotFoundException {
        final boolean emailStatus = true;
        String credStr = cookieParser.parse(cookieStr).getCredential();
        LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(credStr);
        com.yahoo.identity.services.account.Account account = loggedInSession.getAccount();

        org.openapitools.model.Account accountModel = new org.openapitools.model.Account();
        accountModel.setUsername(account.getUsername());
        accountModel.setFirstName(account.getFirstName());
        accountModel.setLastName(account.getLastName());
        accountModel.setEmail(account.getEmail());
        accountModel.setDescription(account.getDescription());

        return Response.status(Response.Status.OK)
            .entity(accountModel)
            .build();
    }

    @Override
    public Response createAccount(org.openapitools.model.Account account, SecurityContext securityContext) throws NotFoundException {
        Session session = identity.getSessionService().newAnonymousSession();

        session.newAccountCreate()
            .setUsername(account.getUsername())
            .setFirstName(account.getFirstName())
            .setLastName(account.getLastName())
            .setEmail(account.getEmail())
            .setPassword(account.getPassword())
            .setDescription(account.getDescription())
            .create();

        logAccountCreation(account);

        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithPassword(account.getUsername(), account.getPassword());

        String cookieStr = loggedInSession.getCredential().toString();
        NewCookie cookie = new NewCookie(Cookies.NAME_CREDENTIAL, cookieStr);

        return Response.status(Response.Status.CREATED)
            .cookie(cookie)
            .build();
    }

    protected void logAccountCreation(org.openapitools.model.Account account) {
        try {
            logger.info("Account {} created", objectMapper.writeValueAsString(account));
        } catch (Exception ex) {
            logger.error("Failed to serialize account info");
        }
    }

    @Override
    public Response updateAccount(String token, String cookieStr, org.openapitools.model.Account account, SecurityContext securityContext)
        throws NotFoundException {
        String credStr = cookieParser.parse(cookieStr).getCredential();
        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithCredential(credStr);
        loggedInSession.validateTokenString(token);

        AccountUpdate accountUpdate = loggedInSession.newAccountUpdate();

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

        return Response.status(Response.Status.NO_CONTENT)
            .build();
    }
}