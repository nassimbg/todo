package com.goldenrealstate.todo.data.client;

public interface ClientFactory {

  BuildingClient getBuildingClient();

  PersonClient getPersonClient();

  TaskClient getTaskClient();
}
