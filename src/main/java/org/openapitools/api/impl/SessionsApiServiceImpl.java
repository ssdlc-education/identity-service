package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.session.SessionCreate;
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
            LoggedInSession loggedInSession = identity.getSessionService()
                .newSessionWithPassword(session.getUsername(), session.getPassword());

            Account account = identity.getAccountService().getAccount(session.getUsername());
            if (!account.verify(session.getPassword())) {
                throw new NotAuthorizedException("Account is locked!");
            }
            String credential = loggedInSession.getCredential();
            ApiResponseMessage successMsg = new ApiResponseMessage(201, "The session is created successfully");
            return Response.ok().entity(successMsg).header("Set-Cookie", credential).build();

        } catch (NotAuthorizedException e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(401, "Invalid request: " + e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }


}
