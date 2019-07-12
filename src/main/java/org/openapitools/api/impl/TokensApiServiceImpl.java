package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.session.LoggedInSession;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.TokensApiService;
import org.openapitools.model.Token;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class TokensApiServiceImpl extends TokensApiService {

    private final Identity identity;

    public TokensApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response createToken(Token tokenModel, SecurityContext securityContext) throws NotFoundException {
        LoggedInSession loggedInSession =
            identity.getSessionService().newSessionWithCredential(securityContext.getAuthenticationScheme());
        com.yahoo.identity.services.token.Token token = loggedInSession.createToken();
        tokenModel.setValue(token.toString());

        return Response.status(Response.Status.CREATED).entity(tokenModel).build();
    }
}