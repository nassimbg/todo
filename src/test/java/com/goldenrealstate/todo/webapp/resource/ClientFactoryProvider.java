package com.goldenrealstate.todo.webapp.resource;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.resource.building.BuildingClientMock;

import java.util.function.Supplier;
import javax.ws.rs.ext.Provider;

@Provider
public class ClientFactoryProvider implements Supplier<ClientFactory> {

  @Override
  public ClientFactory get() {
    return new ClientFactoryMock();
  }

  private static final class ClientFactoryMock implements ClientFactory {

    private final BuildingClient buildingClient;

    private ClientFactoryMock() {
      buildingClient = new BuildingClientMock();
    }

    @Override
    public BuildingClient getBuildingClient() {
      return buildingClient;
    }
  }
}
