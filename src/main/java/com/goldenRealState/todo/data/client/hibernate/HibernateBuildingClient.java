package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.model.hibernate.BuildingEntity;
import com.goldenrealstate.todo.webapp.models.building.Building;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.toUUID;

public class HibernateBuildingClient implements BuildingClient {

  private final EntityManager em;
  private final Class<BuildingEntity> entityClass;

  private HibernateBuildingClient(EntityManager em) {
    this.em = em;
    this.entityClass = BuildingEntity.class;
  }

  public static HibernateBuildingClient create(EntityManager session) {
    return new HibernateBuildingClient(session);
  }

  @Override
  public String post(final Building ob) {
    final BuildingEntity buildingHibernate = toHibernate(ob);

    return executeWriteInTransaction(() -> {
      em.persist(buildingHibernate);

      return buildingHibernate.getId().toString();
    }, em);
  }

  @Override
  public Building get(final String id) throws NotFoundException {
    return toDTO(getOrElseThrow(id));
  }

  private static BuildingEntity toHibernate(Building building) {
    return new BuildingEntity(building.getName());
  }

  private BuildingEntity getOrElseThrow(final String id) {
    final BuildingEntity buildingHibernate = HibernateUtil.executeReadInTransaction(() -> em.find(entityClass, toUUID(id), LockModeType.OPTIMISTIC), em);

    if (buildingHibernate == null) {
      throw new NotFoundException("building with id '" + id + "' is not found");
    }
    return buildingHibernate;
  }

  private static Building toDTO(BuildingEntity buildingHibernate) {
    final Building building = new Building(buildingHibernate.getName());

    building.setId(HibernateUtil.toString(buildingHibernate.getId()));
    return building;
  }
}
