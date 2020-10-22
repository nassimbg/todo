package com.goldenrealstate.todo.webapp.resource.task;

import com.goldenrealstate.todo.data.client.ClientFactory;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.webapp.models.Id;
import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static com.goldenrealstate.todo.webapp.util.PathBuilder.TASKS;

@Path(TASKS)
public final class TaskResource {
  private static final String TASK_ID = "taskId";

  private final TaskClient taskClient;

  public TaskResource(@Context ClientFactory clientFactory) {
    this.taskClient = clientFactory.getTaskClient();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Id createTask(Task task) {
    return Id.fromString(taskClient.post(task));
  }

  @Path("{" + TASK_ID + "}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Task getTask(@PathParam(TASK_ID) String id) {
    return taskClient.get(id);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Collection<Task> getTasks(@QueryParam("assignee_id") String assigneeId, @QueryParam("building_id") String buildingId) {
    return taskClient.getAll(assigneeId, buildingId);
  }

  @PUT
  @Path("{" + TASK_ID + "}")
  @Consumes(MediaType.APPLICATION_JSON)
  public void updateTask(@PathParam(TASK_ID) String id, Task task){
    taskClient.put(id, task);
  }
}
