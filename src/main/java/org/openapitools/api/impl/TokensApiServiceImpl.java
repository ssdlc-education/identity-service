package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.IdentityError;
import com.yahoo.identity.IdentityException;
import com.yahoo.identity.services.session.LoggedInSession;
import com.yahoo.identity.services.token.TokenCreate;
import com.yahoo.identity.services.token.TokenType;
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
    public Response createToken(Token token, SecurityContext securityContext) throws NotFoundException {
        try {
            LoggedInSession
                loggedInSession =
                identity.getSessionService().newSessionWithCredential(securityContext.getAuthenticationScheme());

            TokenCreate tokenCreate = identity.getTokenService().newTokenCreate();
            switch (token.getType()) {
                case CRITICAL:
                    tokenCreate.setTokenType(TokenType.CRITICAL);
                    break;
                case STANDARD:
                    tokenCreate.setTokenType(TokenType.STANDARD);
                    break;
                default:
                    throw new IdentityException(IdentityError.INVALID_ARGUMENTS, "Unsupported token type.");
            }
            tokenCreate.initToken(loggedInSession.getUsername());
            token.setValue(tokenCreate.create().toString());

            return Response.status(Response.Status.CREATED).entity(token).build();

        } catch (IdentityException e) {
            switch (e.getError()) {
                case ACCOUNT_NOT_FOUND:
                    return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
                case INVALID_ARGUMENTS:
                    return Response.status(Response.Status.BAD_REQUEST).entity(e.toString()).build();
                case INVALID_CREDENTIAL:
                    return Response.status(Response.Status.UNAUTHORIZED).entity(e.toString()).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.toString()).build();
            }
        }
    }
}