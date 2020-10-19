package com.goldenrealstate.todo.webapp.models.person;

import java.util.Objects;

public final class Person {

  private final String name;
  private String id;

  private Person() {
    name = "";
  }

  public Person(final String name) {
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Person building = (Person) o;
    return Objects.equals(name, building.name) &&
        Objects.equals(id, building.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, id);
  }
}
