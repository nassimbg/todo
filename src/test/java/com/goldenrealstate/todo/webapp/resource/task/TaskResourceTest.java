package com.goldenrealstate.todo.webapp.resource.task;


import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.task.Status;
import com.goldenrealstate.todo.webapp.models.task.Task;
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

public class TaskResourceTest extends AbstractJerseyTest {

  @Override
  protected ResourceConfig configure() {
    final ResourceConfig resourceConfig = super.configure();

    resourceConfig.register(new AbstractBinder() {
      @Override
      protected void configure() {
        bindFactory(ClientFactoryProvider.class).to(ClientFactory.class).in(Singleton.class);
      }
    });
    resourceConfig.register(TaskResource.class);
    return resourceConfig;
  }

  @Test
  public void testCreateTask() {
    postAndAssertTask("-1155869325", "task 1");
  }

  @Test
  public void testGetForExistingTask() {
    final Task task = postAndAssertTask("-1155869325", "task 1");

    final Task retrievedTask = target(PathBuilder.buildPath(PathBuilder.TASKS, task.getId()))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Task.class);

    assertEquals(task, retrievedTask);
  }

  @Test
  public void testPutForExistingTask() {
    final Task task = postAndAssertTask("-1155869325", "task 1");
    assertEquals(Status.DONE, task.getStatus());

    task.setStatus(Status.NOT_STARTED);

    final Response putResponse = target(PathBuilder.buildPath(PathBuilder.TASKS, task.getId()))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .put(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.NO_CONTENT.getStatusCode(), putResponse.getStatus());

    final Task retrievedTask = target(PathBuilder.buildPath(PathBuilder.TASKS, task.getId()))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Task.class);

    assertEquals(task, retrievedTask);
  }

  @Test
  public void testGetForNonExistingTask() {
    final String id = "-1155869325";

    final Response response = target(PathBuilder.buildPath(PathBuilder.TASKS, id))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get();

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    assertEquals("Task with id " + id + " not found", response.readEntity(String.class));
  }

  @Test
  public void testGetAllTasks() {
    final String id = "-1155869325";
    final String name = "task 1";
    final Task task = postAndAssertTask(id, name);

    final String id2 = "431529176";
    final String name2 = "task 1";
    final Task task2 = postAndAssertTask(id2, name2);

    final Collection<Task> retrievedTasks = target(PathBuilder.TASKS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Collection<Task>>() {});

    assertEquals(2, retrievedTasks.size());
    assertTrue(retrievedTasks.contains(task));
    assertTrue(retrievedTasks.contains(task2));
  }

  @Test
  public void shouldGetAllExistingTaskFilterOnBuilding() {
    final String buildingId1 = "222222";
    final String buildingId2 = "33333";

    final String name = "task 1";
    final Task task = postAndAssertTask("-1155869325", name, buildingId1, null);

    final String name2 = "task 2";
    final Task task2 = postAndAssertTask("431529176", name2, buildingId1, null);

    final String name3 = "task 3";
    final Task task3 = postAndAssertTask("1761283695", name3, buildingId2, null);

    final Collection<Task> retrievedTasks = target(PathBuilder.TASKS)
        .queryParam("building_id", buildingId1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Collection<Task>>() {});

    assertEquals(2, retrievedTasks.size());
    assertTrue(retrievedTasks.contains(task));
    assertTrue(retrievedTasks.contains(task2));
  }

  @Test
  public void shouldGetAllExistingTaskFilterOnPerson() {
    final String assigneeId1 = "222222";
    final String assigneeId2 = "33333";

    final String name = "task 1";
    final Task task = postAndAssertTask("-1155869325", name, null, assigneeId1);

    final String name2 = "task 2";
    final Task task2 = postAndAssertTask("431529176", name2, null, assigneeId2);

    final String name3 = "task 3";
    final Task task3 = postAndAssertTask("1761283695", name3, null, assigneeId2);

    final Collection<Task> retrievedTasks = target(PathBuilder.TASKS)
        .queryParam("assignee_id", assigneeId2)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Collection<Task>>() {});

    assertEquals(2, retrievedTasks.size());
    assertTrue(retrievedTasks.contains(task3));
    assertTrue(retrievedTasks.contains(task2));
  }

  @Test
  public void shouldGetAllExistingTaskFilterOnPersonAndBuilding() {
    final String assigneeId1 = "222222";
    final String assigneeId2 = "33333";
    final String buildingId = "4444";

    final String name = "task 1";
    final Task task = postAndAssertTask("-1155869325", name, buildingId, assigneeId1);

    final String name2 = "task 2";
    final Task task2 = postAndAssertTask("431529176", name2, "5555", assigneeId2);

    final String name3 = "task 3";
    final Task task3 = postAndAssertTask("1761283695", name3, "6666", assigneeId2);

    final Collection<Task> retrievedTasks = target(PathBuilder.TASKS)
        .queryParam("building_id", buildingId)
        .queryParam("assignee_id", assigneeId1)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(new GenericType<Collection<Task>>() {});

    assertEquals(1, retrievedTasks.size());
    assertTrue(retrievedTasks.contains(task));
  }

  private Task postAndAssertTask(final String id, final String name, String building, final String assigneeId) {
    final Task task = new Task(name, Status.DONE);
    task.setBuildingId(building);
    task.setAssigneeId(assigneeId);

    final Response postResponse = target(PathBuilder.TASKS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

    task.setId(id);
    assertEquals(Response.Status.OK.getStatusCode(), postResponse.getStatus());
    assertEquals(id, postResponse.readEntity(Id.class).getId());
    return task;
  }

  private Task postAndAssertTask(final String id, final String name) {
    return postAndAssertTask(id, name, null, null);
  }
}
