package com.goldenrealstate.todo.webapp;


import com.goldenrealstate.todo.data.client.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundMapper extends AbstractExceptionMapper<NotFoundException> {

  @Override
  public Response.Status getResponseStatus() {
    return Response.Status.NOT_FOUND;
  }
}
