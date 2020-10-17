package com.goldenrealstate.todo.data.client.hibernate;

import org.glassfish.jersey.internal.inject.DisposableSupplier;
import org.hibernate.Session;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public final class EntityManagerProvider implements DisposableSupplier<Session> {

    private final EntityManagerFactory managerFactory;

    public EntityManagerProvider(@Context EntityManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public Session get() {
        return (Session) managerFactory.createEntityManager();
    }

    @Override
    public void dispose(Session instance) {
        instance.close();
    }
}
