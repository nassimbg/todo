package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.PersonClient;

import javax.persistence.EntityManager;

public class HibernateClientFactory implements ClientFactory {

  private final HibernateBuildingClient buildingClient;
  private final HibernatePersonClient personHibernate;

  public HibernateClientFactory(final EntityManager entityManager) {
    buildingClient = HibernateBuildingClient.create(entityManager);
    personHibernate = HibernatePersonClient.create(entityManager);
  }

  @Override
  public BuildingClient getBuildingClient() {
    return buildingClient;
  }

  @Override
  public PersonClient getPersonClient() {
    return personHibernate;
  }
}
