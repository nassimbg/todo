package com.goldenrealstate.todo.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

  private static final Logger LOGGER = LoggerFactory.getLogger("HTTP");

  @Override
  public Response toResponse(final T exception) {
    LOGGER.error("error occurred", exception);

    String message = exception.getMessage() == null ? "" : exception.getMessage();
    final Response.Status responseStatus = getResponseStatus();
    return Response.status(responseStatus).entity(message).build();
  }

  public abstract Response.Status getResponseStatus();
}
