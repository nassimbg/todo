package com.goldenrealstate.todo.webapp.models.task;

import java.util.Objects;

import static com.goldenrealstate.todo.webapp.models.task.Status.NOT_STARTED;

public final class Task {

  private String id;
  private final String name;
  private Status status;
  private String assigneeId;
  private String buildingId;

  private Task() {
    name = "";
    status = NOT_STARTED;
  }

  public Task(final String name, final Status status) {
    Objects.requireNonNull(name);

    this.name = name;
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public void setAssigneeId(final String assigneeId) {
    this.assigneeId = assigneeId;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  public void setBuildingId(final String buildingId) {
    this.buildingId = buildingId;
  }

  public String getAssigneeId() {
    return assigneeId;
  }

  public String getBuildingId() {
    return buildingId;
  }

  public Status getStatus() {
    return status;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Task task = (Task) o;
    return Objects.equals(id, task.id) &&
        Objects.equals(name, task.name) &&
        status == task.status &&
        Objects.equals(assigneeId, task.assigneeId) &&
        Objects.equals(buildingId, task.buildingId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, status, assigneeId, buildingId);
  }
}
