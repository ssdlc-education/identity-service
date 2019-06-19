package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.session.SessionCreate;
import com.yahoo.identity.services.storage.sql.PasswordAbuseDetection;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.SessionsApiService;
import org.openapitools.model.Session;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
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
            SessionCreate sessionCreate = identity.getSessionService().newSessionCreate();
            sessionCreate.setUsername(session.getUsername());
            sessionCreate.setPassword(session.getPassword());
            sessionCreate.initCredential();

            PasswordAbuseDetection passwordAbuseDetection = new PasswordAbuseDetection(identity);

            if (passwordAbuseDetection.abuseDetection(sessionCreate.getUsername(), sessionCreate.getPassword())) {
                throw new NotAuthorizedException("Password abuse detected.");
            }

            String token = sessionCreate.create();
            ApiResponseMessage successMsg = new ApiResponseMessage(201, "The session is created successfully");
            return Response.ok().entity(successMsg).header("Set-Cookie", token).build();

        } catch (BadRequestException e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(401, "Invalid request: " + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs" + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }


}
