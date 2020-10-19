package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.building.Building;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBuildingClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    Utils.withEntityManager(em -> {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = new Building("building 1");

      final String post = hibernateBuildingClient.post(building);

      assertNotNull(post);
    });
  }

  @Test
  void shouldPersistAndGetBuilding() {
    Utils.withEntityManager(em -> {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = new Building("building 1");

      final String id = hibernateBuildingClient.post(building);

      assertNotNull(id);
      building.setId(id);

      final Building retrievedBuilding = hibernateBuildingClient.get(id);
      assertEquals(building, retrievedBuilding);
    });
  }

  @Test
  void shouldFailSinceNotExistingBuilding() {
    assertThrows(NotFoundException.class, () -> {
      Utils.withEntityManager(em -> {
        final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);

        hibernateBuildingClient.get("0e3cf451-602f-4c18-9832-1f20399ee1cd");
      });
    });
  }
}
