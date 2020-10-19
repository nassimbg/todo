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

import javax.inject.Singleton;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

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
    final Task task = new Task("task 1", Status.NOT_STARTED);

    final Response response = target(PathBuilder.TASKS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    assertEquals("-1155869325", response.readEntity(Id.class).getId());
  }

  @Test
  public void testGetForExistingTask() {
    final Task task = new Task("task 1", Status.DONE);

    final Response postResponse = target(PathBuilder.TASKS)
        .request(MediaType.APPLICATION_JSON_TYPE)
        .post(Entity.entity(task, MediaType.APPLICATION_JSON_TYPE));

    assertEquals(Response.Status.OK.getStatusCode(), postResponse.getStatus());

    final String id = "-1155869325";
    assertEquals(id, postResponse.readEntity(Id.class).getId());

    final Task retrievedTask = target(PathBuilder.buildPath(PathBuilder.TASKS, id))
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Task.class);

    task.setId(id);
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
}
