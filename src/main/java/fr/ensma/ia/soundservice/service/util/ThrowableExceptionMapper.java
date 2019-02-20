package fr.ensma.ia.soundservice.service.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable ex) {
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse();
        } else {
            ex.printStackTrace();
            return Response.status(500).entity(ex.getMessage()).type("text/plain").build();
        }
    }
}
