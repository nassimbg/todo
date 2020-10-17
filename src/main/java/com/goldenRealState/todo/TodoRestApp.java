package com.goldenrealstate.todo;

import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.hibernate.ClientFactoryProvider;
import com.goldenrealstate.todo.data.client.hibernate.EntityManagerProvider;
import com.goldenrealstate.todo.data.client.hibernate.HibernateUtil;
import com.goldenrealstate.todo.webapp.resource.building.BuildingResource;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.internal.inject.DisposableSupplier;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.ext.Provider;

public class TodoRestApp extends ResourceConfig {

  public TodoRestApp() {
    // register Features
    register(JacksonFeature.class);

    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(JPAResolver.class).to(EntityManagerFactory.class).in(Singleton.class);
        bindFactory(EntityManagerProvider.class).to(EntityManager.class).in(RequestScoped.class);
        bindFactory(ClientFactoryProvider.class).to(ClientFactory.class).in(RequestScoped.class);

      }
    });

    // register Resources
    register(BuildingResource.class);
  }

  @Provider
  public static class JPAResolver implements DisposableSupplier<EntityManagerFactory> {

    @Override
    public void dispose(EntityManagerFactory entityManagerFactory) {
      HibernateUtil.reset(entityManagerFactory);
    }

    @Override
    public EntityManagerFactory get() {
      return HibernateUtil.getSessionJavaConfigFactory();
    }
  }
}
