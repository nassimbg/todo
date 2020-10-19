package com.goldenrealstate.todo.webapp.resource;

import com.goldenrealstate.todo.webapp.NotFoundMapper;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

public abstract class AbstractJerseyTest extends JerseyTest {

  @Override
  protected ResourceConfig configure() {
    final ResourceConfig resourceConfig = new ResourceConfig();

    resourceConfig.register(NotFoundMapper.class);
    return resourceConfig;
  }
}
