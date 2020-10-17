package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.webapp.models.building.Building;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBuildingClientTest {

  @Test
  void should_persist_and_return_generated_id() {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("dao_test", System.getProperties());

    try {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(emf.createEntityManager());
      final Building building = new Building("building 1");

      final String post = hibernateBuildingClient.post(building);

      assertNotNull(post);
    } finally {
      emf.close();
    }
  }
}
