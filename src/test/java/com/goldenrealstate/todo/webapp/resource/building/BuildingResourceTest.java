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

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

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
    final Building building = new Building("building 1");

    final Response response = target(PathBuilder.BUILDINGS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(building, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals("-1155869325", response.readEntity(Id.class).getId());
  }

  @Test
  public void testGetForExistingBuilding() {
    final Building building = new Building("building 1");

    final Response postResponse = target(PathBuilder.BUILDINGS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(building, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), postResponse.getStatus());

    final String buildingId = "-1155869325";
    assertEquals(buildingId, postResponse.readEntity(Id.class).getId());

    final Building retrievedBuilding = target(PathBuilder.buildPath(PathBuilder.BUILDINGS, buildingId))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Building.class);

    building.setId(buildingId);
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
}
