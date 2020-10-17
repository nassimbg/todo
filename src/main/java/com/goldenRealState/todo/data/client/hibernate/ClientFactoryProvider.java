package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.ClientFactory;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientFactoryProvider implements Supplier<ClientFactory> {

  private final EntityManager session;

  public ClientFactoryProvider(@Context EntityManager session) {
    this.session = session;
  }

  @Override
  public ClientFactory get() {
    return new HibernateClientFactory(session);
  }
}
