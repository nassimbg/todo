package com.goldenrealstate.todo.webapp.resource.building;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.building.Building;

import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static com.goldenrealstate.todo.webapp.util.PathBuilder.BUILDINGS;

@Path(BUILDINGS)
public final class BuildingResource {
  private static final String BUILDING_ID = "buildingId";

  private final BuildingClient buildingClient;

  public BuildingResource(@Context ClientFactory clientFactory) {
    this.buildingClient = clientFactory.getBuildingClient();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Id createBuilding(Building building) {
    return Id.fromString(buildingClient.post(building));
  }

  @Path("{" + BUILDING_ID + "}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Building getBuilding(@PathParam(BUILDING_ID) String id) {
    return buildingClient.get(id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Building> getBuildings() {
    return buildingClient.getAll();
  }
}
