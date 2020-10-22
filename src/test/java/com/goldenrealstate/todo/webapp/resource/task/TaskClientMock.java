package com.goldenrealstate.todo.webapp.resource.task;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public final class TaskClientMock implements TaskClient {

  private final Map<String, Task> cache;
  private final Random random;

  public TaskClientMock() {
    cache = new HashMap<>();
    random = new Random(1);
  }

  @Override
  public String post(final Task ob) {
    final String id = String.valueOf(random.nextInt());
    ob.setId(id);
    cache.put(id, ob);
    return id;
  }

  @Override
  public Task get(final String id) throws NotFoundException {
    final Task task = cache.get(id);

    if (task == null) {
      throw new NotFoundException("Task with id " + id + " not found");
    }
    return task;
  }

  @Override
  public Collection<Task> getAll() {
    return cache.values();
  }

  @Override
  public Collection<Task> getAll(final String assigneeId, final String buildingId) {
    final boolean isAssigneeIdNull = isNullOrEmpty(assigneeId);
    final boolean isBuildingIdNull = isNullOrEmpty(buildingId);
    return getAll()
        .stream()
        .filter(task -> (isAssigneeIdNull || assigneeId.equals(task.getAssigneeId())) &&
            (isBuildingIdNull || buildingId.equals(task.getBuildingId())))
        .collect(Collectors.toList());
  }

  @Override
  public void put(final String id, final Task task) {
    cache.put(id, task);
  }

  private static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}
