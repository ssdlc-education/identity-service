package org.openapitools.api.impl;

import com.yahoo.identity.Identity;
import org.openapitools.api.AccountsApiService;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.model.Account;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class AccountsApiServiceImpl extends AccountsApiService {

    private final Identity identity;

    public AccountsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response accountsIdGet(String id, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response accountsPost(Account account, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response accountsmePut(String token, Account account, SecurityContext securityContext) throws NotFoundException {
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
