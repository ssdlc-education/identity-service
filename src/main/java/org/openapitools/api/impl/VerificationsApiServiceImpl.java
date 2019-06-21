package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.Account;
import com.yahoo.identity.services.challenge.ChallengeSubmit;
import com.yahoo.identity.services.session.LoggedInSession;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.VerificationsApiService;
import org.openapitools.model.Verification;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class VerificationsApiServiceImpl extends VerificationsApiService {

    private final Identity identity;

    public VerificationsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response verificationsPost(String cookie, Verification verification, SecurityContext securityContext)
        throws NotFoundException {
        try {
            if (!verification.getUsername().isEmpty()) {
                LoggedInSession loggedInSession = identity.getSessionService().newSessionWithCredential(cookie);
                Account account = loggedInSession.getAccount();

                ChallengeSubmit challengeSubmit = identity.getVerificationService().newChallengeSubmit();
                challengeSubmit.setUsername(account.getUsername());
                challengeSubmit.setEmail(account.getEmail());
                challengeSubmit.submit();
            } else {
                ChallengeSubmit challengeSubmit = identity.getVerificationService().newChallengeSubmit();
                challengeSubmit.setUsername(verification.getUsername());
                challengeSubmit.setEmail(verification.getValue());
                challengeSubmit.submit();
            }

            ApiResponseMessage
                successMsg =
                new ApiResponseMessage(Response.Status.CREATED.getStatusCode(), "The session is created successfully");
            return Response.status(Response.Status.CREATED).entity(successMsg).build();

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
                                       "Invalid credential or token (including expired cases): " + e.toString());
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e) {
            ApiResponseMessage
                errorMsg =
                new ApiResponseMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                                       "Unknown error occurs: " + e.toString());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }

    @Override
    public Response verificationsPut(Verification verification, SecurityContext securityContext)
        throws NotFoundException {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
