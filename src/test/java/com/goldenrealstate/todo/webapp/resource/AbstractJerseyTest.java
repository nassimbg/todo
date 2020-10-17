package com.goldenrealstate.todo.webapp.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

public abstract class AbstractJerseyTest extends JerseyTest {

  @Override
  protected ResourceConfig configure() {
    return new ResourceConfig();
  }
}
