package org.openapitools.api.impl;

import com.verizonmedia.identity.Identity;
import com.verizonmedia.identity.services.session.LoggedInSession;
import org.openapitools.api.Cookies;
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
    public Response createSession(Session session, SecurityContext securityContext) throws NotFoundException {
        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithPassword(session.getUsername(), session.getPassword());

        String cookieStr = loggedInSession.getCredential().toString();
        NewCookie cookie = new NewCookie(Cookies.NAME_CREDENTIAL, cookieStr);

        Session resp = new Session();
        return Response.status(Response.Status.CREATED).entity(resp).cookie(cookie)
            .build();
    }
}
