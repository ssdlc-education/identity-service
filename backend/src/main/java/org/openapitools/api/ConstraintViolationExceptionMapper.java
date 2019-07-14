package org.openapitools.api;

import com.verizonmedia.identity.IdentityError;
import org.openapitools.model.Error;

import javax.annotation.Nonnull;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Error err = new Error()
            .code(IdentityError.INVALID_ARGUMENTS.getCode())
            .message(prepareMessage(e));
        return Response.status(Response.Status.BAD_REQUEST)
            .entity(err)
            .build();
    }

    @Nonnull
    private String prepareMessage(@Nonnull ConstraintViolationException exception) {
        StringBuilder msgBuilder = new StringBuilder();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            msgBuilder.append(cv.getPropertyPath())
                .append(" ")
                .append(cv.getMessage())
                .append('\n');
        }
        return msgBuilder.toString();
    }
}
