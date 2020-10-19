package com.goldenrealstate.todo.webapp.resource.task;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
}
