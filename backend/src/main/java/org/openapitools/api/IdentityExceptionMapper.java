// Copyright 2019, Oath Inc.
// Licensed under the terms of the MIT license. See LICENSE file in project root for terms.
package org.openapitools.api;

import com.verizonmedia.identity.IdentityException;
import org.openapitools.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IdentityExceptionMapper implements ExceptionMapper<IdentityException> {

    private static final Logger logger = LoggerFactory.getLogger(IdentityExceptionMapper.class);
    private static final int ERROR_CODE_TO_STATUS_POWER = 1000;

    @Override
    public Response toResponse(IdentityException e) {
        logger.debug("Exception is thrown: ", e);
        Error err = new Error()
            .code(e.getError().getCode())
            .message(e.getMessage());
        return Response.status(e.getError().getCode() / ERROR_CODE_TO_STATUS_POWER)
            .entity(err)
            .build();
    }
}
