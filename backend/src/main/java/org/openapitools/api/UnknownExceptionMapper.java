package org.openapitools.api;

import com.yahoo.identity.IdentityError;
import org.openapitools.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(UnknownExceptionMapper.class);

    @Override
    public Response toResponse(Throwable throwable) {
        logger.error("Unknown exception is thrown: ", throwable);
        IdentityError identityError = IdentityError.INTERNAL_SERVER_ERROR;
        Error err = new Error()
            .code(identityError.getCode())
            .message(throwable.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .entity(err)
            .build();
    }
}
