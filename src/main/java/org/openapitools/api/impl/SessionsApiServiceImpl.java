package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.session.LoggedInSession;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.SessionsApiService;
import org.openapitools.model.Session;

import javax.annotation.Nonnull;
import javax.ws.rs.NotAuthorizedException;
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
            String token = loggedInSession.getCredential().toString();

            ApiResponseMessage
                successMsg =
                new ApiResponseMessage(Response.Status.CREATED.getStatusCode(), "The session is created successfully");
            return Response.status(Response.Status.CREATED).entity(successMsg).header("Set-Cookie", token).build();

        } catch (NotAuthorizedException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
                                       "Invalid request: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }


}
