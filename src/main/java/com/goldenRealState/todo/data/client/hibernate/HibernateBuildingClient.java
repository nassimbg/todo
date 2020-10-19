package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.model.hibernate.BuildingEntity;
import com.goldenrealstate.todo.webapp.models.building.Building;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.findOrElseThrow;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.getAllQuery;

public class HibernateBuildingClient implements BuildingClient {
  private static final Function<String, String> NOT_FOUND_ERROR_MSG = id -> "Building with id '" + id + "' is not found";

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
    return toModel(findOrElseThrow(em, entityClass, id, NOT_FOUND_ERROR_MSG));
  }

  @Override
  public Collection<Building> getAll() {
    TypedQuery<BuildingEntity> allQuery = getAllQuery(BuildingEntity.class, em);
    return allQuery.getResultList()
        .stream()
        .map(HibernateBuildingClient::toModel)
        .collect(Collectors.toList());
  }

  private static BuildingEntity toHibernate(Building building) {
    return new BuildingEntity(building.getName());
  }

  private static Building toModel(BuildingEntity buildingHibernate) {
    final Building building = new Building(buildingHibernate.getName());

    building.setId(HibernateUtil.toString(buildingHibernate.getId()));
    return building;
  }
}
