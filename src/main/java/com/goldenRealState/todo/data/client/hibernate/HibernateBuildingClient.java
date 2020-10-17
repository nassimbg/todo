package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.model.hibernate.BuildingEntity;
import com.goldenrealstate.todo.webapp.models.building.Building;

import javax.persistence.EntityManager;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;

public class HibernateBuildingClient implements BuildingClient {

  private final EntityManager session;

  private HibernateBuildingClient(EntityManager session) {
    this.session = session;
  }

  public static HibernateBuildingClient create(EntityManager session) {
    return new HibernateBuildingClient(session);
  }

  @Override
  public String post(final Building ob) {
    final BuildingEntity buildingEntity = toHibernate(ob);

    return executeWriteInTransaction(() -> {
      session.persist(buildingEntity);

      return buildingEntity.getId().toString();
    }, session);
  }

  private static BuildingEntity toHibernate(Building building) {
    return new BuildingEntity(building.getName());
  }
}
