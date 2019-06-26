package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.token.TokenCreate;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.TokensApiService;
import org.openapitools.model.Token;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class TokensApiServiceImpl extends TokensApiService {

    private final Identity identity;

    public TokensApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response tokensPost(Token token, SecurityContext securityContext) throws NotFoundException {
        try {

            TokenCreate tokenCreate = identity.getTokenService().newTokenCreate();
            tokenCreate.setToken(token.getValue());
            tokenCreate.setType(token.getType());

            tokenCreate.create();

            ApiResponseMessage
                successMsg =
                new ApiResponseMessage(Response.Status.CREATED.getStatusCode(), "The session is created successfully");
            return Response.status(Response.Status.CREATED).entity(successMsg).header("Set-Cookie", token).build();

        } catch (BadRequestException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.BAD_REQUEST.getStatusCode(),
                                       "Invalid request or credential too old: " + e.toString());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
        } catch (NotAuthorizedException e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.UNAUTHORIZED.getStatusCode(),
                                       "Invalid credential: " + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs: " + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }
}
