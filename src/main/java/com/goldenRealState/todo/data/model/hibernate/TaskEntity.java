package com.goldenrealstate.todo.data.model.hibernate;

import com.goldenrealstate.todo.webapp.models.task.Status;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity(name ="todo_task")
@Table(name = "TODO_TASKS")
public class TaskEntity implements Serializable {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  @Column(name = "ID", updatable = false, nullable = false)
  private UUID id;

  @Version
  @Column(name = "VERSION")
  private long version;

  @Column(name = "NAME", length = 127)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Status status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PERSON_ID")
  private PersonEntity person;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BUILDING_ID")
  private BuildingEntity building;

  public TaskEntity(String name) {
    this.name = name;
  }

  public TaskEntity() {
   //do nothing
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setPerson(final PersonEntity person) {
    this.person = person;
  }

  public void setBuilding(final BuildingEntity building) {
    this.building = building;
  }

  public PersonEntity getPerson() {
    return person;
  }

  public BuildingEntity getBuilding() {
    return building;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(final Status status) {
    this.status = status;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final TaskEntity that = (TaskEntity) o;
    return version == that.version &&
        Objects.equals(id, that.id) &&
        Objects.equals(name, that.name) &&
        status == that.status &&
        Objects.equals(person, that.person) &&
        Objects.equals(building, that.building);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version, name, status, person, building);
  }
}
