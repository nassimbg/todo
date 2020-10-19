package com.goldenrealstate.todo.webapp.resource;

import com.goldenrealstate.todo.data.client.BuildingClient;
import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.PersonClient;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.webapp.resource.building.BuildingClientMock;
import com.goldenrealstate.todo.webapp.resource.person.PersonClientMock;
import com.goldenrealstate.todo.webapp.resource.task.TaskClientMock;

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
    private final PersonClient personClient;
    private final TaskClientMock taskClient;

    private ClientFactoryMock() {
      buildingClient = new BuildingClientMock();
      personClient = new PersonClientMock();
      taskClient = new TaskClientMock();
    }

    @Override
    public BuildingClient getBuildingClient() {
      return buildingClient;
    }

    @Override
    public PersonClient getPersonClient() {
      return personClient;
    }

    @Override
    public TaskClient getTaskClient() {
      return taskClient;
    }
  }
}
