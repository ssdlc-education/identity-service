// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api.impl;

import com.verizonmedia.identity.Identity;
import org.openapitools.api.ApiResponseMessage;
import org.openapitools.api.NotFoundException;
import org.openapitools.api.VerificationsApiService;
import org.openapitools.model.Verification;

import javax.annotation.Nonnull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJerseyServerCodegen", date = "2019-05-14T20:17:48.996+08:00[Asia/Taipei]")
public class VerificationsApiServiceImpl extends VerificationsApiService {

    private final Identity identity;

    public VerificationsApiServiceImpl(@Nonnull Identity identity) {
        this.identity = identity;
    }

    @Override
    public Response createVerification(Verification verification, SecurityContext securityContext)
        throws NotFoundException {
        // TODO Not yet implemented
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }

    @Override
    public Response updateVerification(Verification verification, SecurityContext securityContext)
        throws NotFoundException {
        // TODO Not yet implemented
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
