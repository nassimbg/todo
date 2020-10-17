package com.goldenrealstate.todo.webapp.models.building;

import java.util.Objects;

public final class Building {

  private final String name;
  private String id;

  private Building() {
    name = "";
  }

  public Building(final String name) {
    Objects.requireNonNull(name);

    this.name = name;
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
}
