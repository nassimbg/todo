package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.building.Building;
import com.goldenrealstate.todo.webapp.models.person.Person;
import com.goldenrealstate.todo.webapp.models.task.Status;
import com.goldenrealstate.todo.webapp.models.task.Task;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import javax.persistence.RollbackException;

import static com.goldenrealstate.todo.data.client.hibernate.Utils.withEntityManager;
import static org.junit.jupiter.api.Assertions.*;

class HibernateTaskClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      postTaskAndAssert(hibernateTaskClient, "task 1", person, building);
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
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      postGetAndAssertTask(hibernateTaskClient, person, building);
    });
  }

  @Test
  void shouldPostToExistingTask() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Task task = postGetAndAssertTask(hibernateTaskClient, person, building);
      task.setStatus(Status.DONE);

      hibernateTaskClient.put(task.getId(), task);

      final Task retrievedTask = hibernateTaskClient.get(task.getId());

      assertEquals("task 1", retrievedTask.getName());
      assertEquals(building.getId(), retrievedTask.getBuildingId());
      assertEquals(person.getId(), retrievedTask.getAssigneeId());
      assertEquals(Status.DONE, retrievedTask.getStatus());
    });
  }

  @Test
  void shouldGetAllExistingTasks() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Task task1 = postTaskAndAssert(hibernateTaskClient, "task 1", person, building);
      final Task task2 = postTaskAndAssert(hibernateTaskClient, "task 2", person, building);


      final Building building2 = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 2");
      final Task task3 = postTaskAndAssert(hibernateTaskClient, "task 3", person, building2);

      final Collection<Task> allTasks = hibernateTaskClient.getAll();
      assertEquals(3, allTasks.size());
      assertTrue(allTasks.contains(task1));
      assertTrue(allTasks.contains(task2));
      assertTrue(allTasks.contains(task3));
    });
  }

  @Test
  void shouldGetAllExistingTaskFilterOnBuilding() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Task task1 = postTaskAndAssert(hibernateTaskClient, "task 1", person, building);
      final Task task2 = postTaskAndAssert(hibernateTaskClient, "task 2", person, building);


      final Building building2 = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 2");
      final Task task3 = postTaskAndAssert(hibernateTaskClient, "task 3", person, building2);

      final Collection<Task> allTasks = hibernateTaskClient.getAll(null, building.getId());
      assertEquals(2, allTasks.size());
      assertTrue(allTasks.contains(task1));
      assertTrue(allTasks.contains(task2));
    });
  }

  @Test
  void shouldGetAllExistingTaskFilterOnPerson() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Task task1 = postTaskAndAssert(hibernateTaskClient, "task 1", person, building);

      final Person person2 = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 2");
      final Task task2 = postTaskAndAssert(hibernateTaskClient, "task 2", person2, building);

      final Building building2 = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 2");
      final Task task3 = postTaskAndAssert(hibernateTaskClient, "task 3", person, building2);

      final Collection<Task> allTasks = hibernateTaskClient.getAll(person2.getId(), null);
      assertEquals(1, allTasks.size());
      assertTrue(allTasks.contains(task2));
    });
  }

  @Test
  void shouldGetAllExistingTaskFilterOnPersonAndBuilding() {
    withEntityManager(em -> {
      final HibernateTaskClient hibernateTaskClient = HibernateTaskClient.create(em);
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 1");

      final HibernateBuildingClient hibernateBuildingClient = HibernateBuildingClient.create(em);
      final Building building = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 1");

      final Task task1 = postTaskAndAssert(hibernateTaskClient, "task 1", person, building);

      final Person person2 = HibernatePersonClientTest.postPersonAndAssert(hibernatePersonClient, "person 2");
      final Task task2 = postTaskAndAssert(hibernateTaskClient, "task 2", person2, building);

      final Building building2 = HibernateBuildingClientTest.postBuildingAndAssert(hibernateBuildingClient, "building 2");
      final Task task3 = postTaskAndAssert(hibernateTaskClient, "task 3", person, building2);
      final Task task4 = postTaskAndAssert(hibernateTaskClient, "task 3", person, building2);

      final Collection<Task> allTasks = hibernateTaskClient.getAll(person.getId(), building2.getId());
      assertEquals(2, allTasks.size());
      assertTrue(allTasks.contains(task3));
      assertTrue(allTasks.contains(task4));
    });
  }

  private Task postTaskAndAssert(final HibernateTaskClient hibernateTaskClient, String taskName, final Person person, final Building building) {
    final Task task = new Task(taskName, Status.NOT_STARTED);
    task.setBuildingId(building.getId());
    task.setAssigneeId(person.getId());
    final String id = hibernateTaskClient.post(task);
    assertNotNull(id);
    task.setId(id);
    return task;
  }

  private Task postGetAndAssertTask(final HibernateTaskClient hibernateTaskClient, final Person person,
      final Building building) {
    final Task task = postTaskAndAssert(hibernateTaskClient, "task 1", person, building);

    final Task retrievedTask = hibernateTaskClient.get(task.getId());

    assertEquals("task 1", retrievedTask.getName());
    assertEquals(building.getId(), retrievedTask.getBuildingId());
    assertEquals(person.getId(), retrievedTask.getAssigneeId());
    assertEquals(Status.NOT_STARTED, retrievedTask.getStatus());

    return retrievedTask;
  }
}
