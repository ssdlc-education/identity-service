package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.session.LoggedInSession;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.SessionsApiService;
import org.openapitools.model.Session;

import javax.annotation.Nonnull;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class SessionsApiServiceImpl extends SessionsApiService {

    private final Identity identity;

    public SessionsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response sessionsPost(Session session, SecurityContext securityContext) throws NotFoundException {
        try {
            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithPassword(session.getUsername(), session.getPassword());

            String cookieStr = loggedInSession.getCredential().toString();
            NewCookie cookie = new NewCookie("V", cookieStr);

            return Response.status(Response.Status.CREATED).entity("The session is created successfully").cookie(cookie)
                .build();

        } catch (IdentityException e) {
            switch (e.getError()) {
                case ACCOUNT_NOT_FOUND:
                    return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
                case INVALID_ARGUMENTS:
                    return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }
}
