package org.openapitools.api.impl;

import com.yahoo.identity.DefaultIdentityFactory;
import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityFactory;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.SessionsApiService;
import org.openapitools.model.Session;

import javax.annotation.Nonnull;
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
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
