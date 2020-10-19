package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.PersonClient;
import com.goldenrealstate.todo.data.client.TaskClient;

import javax.persistence.EntityManager;

public class HibernateClientFactory implements ClientFactory {

  private final HibernateBuildingClient buildingClient;
  private final HibernatePersonClient personClient;
  private final HibernateTaskClient taskClient;

  public HibernateClientFactory(final EntityManager entityManager) {
    buildingClient = HibernateBuildingClient.create(entityManager);
    personClient = HibernatePersonClient.create(entityManager);
    taskClient = HibernateTaskClient.create(entityManager);
  }

  @Override
  public BuildingClient getBuildingClient() {
    return buildingClient;
  }

  @Override
  public PersonClient getPersonClient() {
    return personClient;
  }

  @Override
  public TaskClient getTaskClient() {
    return taskClient;
  }
}
