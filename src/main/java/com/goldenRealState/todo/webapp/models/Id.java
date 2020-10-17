package com.goldenrealstate.todo.webapp.models;

public final class Id {

  private final String id;

  private Id(){
    id = null;
  }

  private Id(final String id) {
    this.id = id;
  }

  public static Id fromString(String id) {
    return new Id(id);
  }

  public String getId() {
    return id;
  }
}
