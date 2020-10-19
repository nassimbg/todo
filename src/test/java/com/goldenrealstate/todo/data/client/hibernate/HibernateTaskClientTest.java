package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.building.Building;
import com.goldenrealstate.todo.webapp.models.person.Person;
import com.goldenrealstate.todo.webapp.models.task.Status;
import com.goldenrealstate.todo.webapp.models.task.Task;

import org.junit.jupiter.api.Test;

import javax.persistence.RollbackException;

import static com.goldenrealstate.todo.data.client.hibernate.Utils.withEntityManager;
import static org.junit.jupiter.api.Assertions.*;

class HibernateTaskClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);

      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = new Person("person 1");

      final String personId = hibernatePersonClient.post(person);

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = new Building("building 1");

      final String buildingId = hibernateBuildingClient.post(building);


      final Task task = new Task("task 1", Status.NOT_STARTED);
      task.setBuildingId(buildingId);
      task.setAssigneeId(personId);

      final String taskId = hibernateTaskClient.post(task);
      assertNotNull(taskId);
    });
  }

  @Test
  void failToPersistIfLinkedBuildingDoesntExist() {
    assertThrows(RollbackException.class, () -> {
      withEntityManager(em -> {
        final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);

        final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
        final Person person = new Person("person 1");

        final String personId = hibernatePersonClient.post(person);

        final Task task = new Task("task 1", Status.NOT_STARTED);
        task.setBuildingId("0e3cf451-602f-4c18-9832-1f20399ee1cd");
        task.setAssigneeId(personId);

        final String taskId = hibernateTaskClient.post(task);
        assertNotNull(taskId);
      });
    });
  }

  @Test
  void shouldFailToGetNotExistingTask() {
    assertThrows(NotFoundException.class, () -> {
      withEntityManager(em -> {
        final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);

        hibernateTaskClient.get("0e3cf451-602f-4c18-9832-1f20399ee1cd");
      });
    });
  }

  @Test
  void shouldGetExistingTask() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = new Person("person 1");

      final String personId = hibernatePersonClient.post(person);

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = new Building("building 1");

      final String buildingId = hibernateBuildingClient.post(building);

      final Task task = new Task("task 1", Status.NOT_STARTED);
      task.setBuildingId(buildingId);
      task.setAssigneeId(personId);

      final String id = hibernateTaskClient.post(task);

      final Task retrievedTask = hibernateTaskClient.get(id);

      assertEquals("task 1", retrievedTask.getName());
      assertEquals(buildingId, retrievedTask.getBuildingId());
      assertEquals(personId, retrievedTask.getAssigneeId());
      assertEquals(Status.NOT_STARTED, retrievedTask.getStatus());
    });
  }
}
