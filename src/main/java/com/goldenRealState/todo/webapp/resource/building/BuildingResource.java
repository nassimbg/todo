package com.goldenrealstate.todo.webapp.resource.building;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.building.Building;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static com.goldenrealstate.todo.webapp.util.PathBuilder.BUILDINGS;

@Path(BUILDINGS)
public final class BuildingResource {

  private final BuildingClient buildingClient;

  public BuildingResource(@Context ClientFactory clientFactory) {
    this.buildingClient = clientFactory.getBuildingClient();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Id createBuilding(Building building) {
    return Id.fromString(buildingClient.post(building));
  }
}
