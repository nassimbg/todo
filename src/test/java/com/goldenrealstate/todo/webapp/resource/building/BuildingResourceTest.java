package com.goldenrealstate.todo.webapp.resource.building;


import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.building.Building;
import com.goldenrealstate.todo.webapp.resource.AbstractJerseyTest;
import com.goldenrealstate.todo.webapp.resource.ClientFactoryProvider;
import com.goldenrealstate.todo.webapp.util.PathBuilder;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;

import java.util.Collection;
import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildingResourceTest extends AbstractJerseyTest {

  @Override
  protected ResourceConfig configure() {
    final ResourceConfig resourceConfig = super.configure();

    resourceConfig.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(ClientFactoryProvider.class).to(ClientFactory.class).in(Singleton.class);
      }
    });
    resourceConfig.register(BuildingResource.class);
    return resourceConfig;
  }

  @Test
  public void testCreateBuildings() {
    getBuilding1AndAssert();
  }

  @Test
  public void testGetForExistingBuilding() {
    final Building building = getBuilding1AndAssert();

    final Building retrievedBuilding = target(PathBuilder.buildPath(PathBuilder.BUILDINGS, building.getId()))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Building.class);

    assertEquals(building, retrievedBuilding);
  }

  @Test
  public void testGetForNonExistingBuilding() {
    final String buildingId = "-1155869325";

    final Response response = target(PathBuilder.buildPath(PathBuilder.BUILDINGS, buildingId))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertEquals("Building with id " + buildingId + " not found", response.readEntity(String.class));
  }

  @Test
  public void testGetForAllPersons() {
    final Building building = getBuilding1AndAssert();
    final Building building2 = getBuildingAndAssert("person 2", "431529176");

    final Collection<Building> buildings = target(PathBuilder.BUILDINGS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Collection<Building>>() {});

    assertEquals(2, buildings.size());
    assertTrue(buildings.contains(building));
    assertTrue(buildings.contains(building2));
  }

  private Building getBuilding1AndAssert() {
    return getBuildingAndAssert("building 1",  "-1155869325");
  }

  private Building getBuildingAndAssert(String name, String id) {
    final Building building = new Building(name);

    final Response postResponse = target(PathBuilder.BUILDINGS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(building, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), postResponse.getStatus());

    assertEquals(id, postResponse.readEntity(Id.class).getId());
    building.setId(id);
    return building;
  }
}
