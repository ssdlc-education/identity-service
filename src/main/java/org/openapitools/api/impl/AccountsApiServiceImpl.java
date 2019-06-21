package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import com.yahoo.identity.services.account.AccountCreate;
import com.yahoo.identity.services.account.AccountUpdate;
import com.yahoo.identity.services.session.SessionCreate;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.Account;

import java.time.Instant;
import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImpl extends AccountsApiService {
    private final Identity identity;

    public AccountsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response accountsIdGet(String username, SecurityContext securityContext) throws NotFoundException {
        String uid = identity.getAccountService().getAccount(username).getUid();
        String firstName = identity.getAccountService().getAccount(username).getFirstName();
        String lastName = identity.getAccountService().getAccount(username).getLastName();
        String email = identity.getAccountService().getAccount(username).getEmail();
        String password = identity.getAccountService().getAccount(username).getPassword();
        Instant createTs = identity.getAccountService().getAccount(username).getCreateTime();
        Instant updateTs = identity.getAccountService().getAccount(username).getUpdateTime();
        String description = identity.getAccountService().getAccount(username).getDescription();

        String msg = email;
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, msg)).build();
    }

    @Override
    public Response accountsPost(Account account, SecurityContext securityContext) throws NotFoundException {
        try {
            Boolean mockVerified = true;

            AccountCreate accountCreate = identity.getAccountService().newAccountCreate();
            accountCreate.setUsername(account.getUsername());
            accountCreate.setFirstName(account.getFirstName());
            accountCreate.setLastName(account.getLastName());
            accountCreate.setEmail(account.getEmail(), mockVerified);

            accountCreate.setPassword(account.getPassword());
            accountCreate.setCreateTime(account.getCreateTime().toInstant());
            accountCreate.setUpdateTime(account.getUpdateTime().toInstant());
            accountCreate.setDescription(account.getDescription());
            accountCreate.create();

            SessionCreate sessionCreate = identity.getSessionService().newSessionCreate();
            sessionCreate.initCredential();

            String token = sessionCreate.create();

            ApiResponseMessage successMsg = new ApiResponseMessage(204, "The account is created successfully.");
            return Response.ok().entity(successMsg).header("Set-Cookie", token).build();
        }catch (BadRequestException e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(400, "Invalid request.");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMsg).build();
        }catch (Exception e) {
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }

    @Override
    public Response accountsmePut(String token, Account account, SecurityContext securityContext) throws NotFoundException {
        Boolean mockVerified = true;
        try {
            SessionCreate sessionCreate = identity.getSessionService().newSessionCreate();
            sessionCreate.setCredential(token);

            AccountUpdate accountUpdate = identity.getAccountService().newAccountUpdate(account.getUsername());
            accountUpdate.setEmail(account.getEmail(), mockVerified);
            accountUpdate.setPassword(account.getPassword());
            accountUpdate.setDescription(account.getDescription());
            accountUpdate.setUpdateTime(account.getUpdateTime().toInstant());
            accountUpdate.update();

            ApiResponseMessage successMsg = new ApiResponseMessage(204, "Successfully upate the account.");
            return Response.ok().entity(successMsg).build();

        }catch (NotAuthorizedException e){
            ApiResponseMessage errorMsg = new ApiResponseMessage(401, "Invalid cookie credential is used.");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorMsg).build();
        } catch (Exception e){
            ApiResponseMessage errorMsg = new ApiResponseMessage(500, "Unknown error occurs.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMsg).build();
        }
    }
}