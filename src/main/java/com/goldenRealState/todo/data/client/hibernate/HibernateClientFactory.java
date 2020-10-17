package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;

import javax.persistence.EntityManager;

public class HibernateClientFactory implements ClientFactory {

  private final HibernateBuildingClient buildingClient;

  public HibernateClientFactory(final EntityManager entityManager) {
    buildingClient = HibernateBuildingClient.create(entityManager);
  }

  @Override
  public BuildingClient getBuildingClient() {
    return buildingClient;
  }
}
