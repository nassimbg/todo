package com.goldenrealstate.todo.data.client;

import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.Collection;

public interface TaskClient extends Client<Task, String> {

  Collection<Task> getAll(String assigneeId, String buildingId);

  void put(String id, Task task);
}
