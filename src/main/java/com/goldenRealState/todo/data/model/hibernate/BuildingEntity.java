package com.goldenrealstate.todo.data.model.hibernate;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "TODO_BUILDINGS")
public class BuildingEntity implements Serializable {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "ID", updatable = false, nullable = false)
  private UUID id;

  @Column(name = "NAME", length = 127)
  private String name;

  @Version
  @Column(name = "VERSION")
  private long version;

  public BuildingEntity(String name) {
    this.name = name;
  }

  public BuildingEntity() {
   //do nothing
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final BuildingEntity that = (BuildingEntity) o;
    return version == that.version &&
        Objects.equals(id, that.id) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, version);
  }
}
