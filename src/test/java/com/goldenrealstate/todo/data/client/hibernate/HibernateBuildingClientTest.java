package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.building.Building;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class HibernateBuildingClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    Utils.withEntityManager(em -> {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      postBuildingAndAssert(hibernateBuildingClient, "building 1");
    });
  }

  @Test
  void shouldPersistAndGetBuilding() {
    Utils.withEntityManager(em -> {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Building retrievedBuilding = hibernateBuildingClient.get(building.getId());
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

  @Test
  void shouldPersistAndGetAllBuildings() {
    Utils.withEntityManager(em -> {
      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = postBuildingAndAssert(hibernateBuildingClient, "building 1");
      final Building building2 = postBuildingAndAssert(hibernateBuildingClient, "building 2");

      final Collection<Building> retrievedBuildings = hibernateBuildingClient.getAll();

      assertEquals(2, retrievedBuildings.size());
      assertTrue(retrievedBuildings.contains(building));
      assertTrue(retrievedBuildings.contains(building2));
    });
  }

  private Building postBuildingAndAssert(final HibernateBuildingClient hibernateBuildingClient, String name) {
    final Building building = new Building(name);

    final String id = hibernateBuildingClient.post(building);

    assertNotNull(id);
    building.setId(id);
    return building;
  }
}
