package com.goldenrealstate.todo.webapp;

import javax.ws.rs.core.Response;

public class DefaultExceptionMapper extends AbstractExceptionMapper<Throwable> {

  @Override
  public Response.Status getResponseStatus() {
    return Response.Status.INTERNAL_SERVER_ERROR;
  }
}
