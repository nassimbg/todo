package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.data.model.hibernate.BuildingEntity;
import com.goldenrealstate.todo.data.model.hibernate.PersonEntity;
import com.goldenrealstate.todo.data.model.hibernate.TaskEntity;
import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.toUUID;

public final class HibernateTaskClient implements TaskClient {

  private static final String ID = "id";
  private static final String FETCH_QUERY =
        "select t "
      + "from todo_task t "
      + "join fetch t.person "
      + "join fetch t.building "
      + "where t.id= :" + ID;

  private final EntityManager em;
  private final Class<TaskEntity> entityClass;

  private HibernateTaskClient(EntityManager em) {
    this.em = em;
    this.entityClass = TaskEntity.class;
  }

  public static HibernateTaskClient create(EntityManager session) {
    return new HibernateTaskClient(session);
  }

  @Override
  public String post(final Task ob) {
    final TaskEntity taskEntity = toHibernate(ob);

    return executeWriteInTransaction(() -> {
      final PersonEntity assignee = em.getReference(PersonEntity.class, toUUID(ob.getAssigneeId()));
      final BuildingEntity assignedBuilding = em.getReference(BuildingEntity.class, toUUID(ob.getBuildingId()));

      taskEntity.setBuilding(assignedBuilding);
      taskEntity.setPerson(assignee);
      taskEntity.setStatus(ob.getStatus());

      em.persist(taskEntity);

      return taskEntity.getId().toString();
    }, em);
  }

  @Override
  public Task get(final String id) throws NotFoundException {
    return toModel(getOrElseThrow(id));
  }

  private static TaskEntity toHibernate(Task task) {
    return new TaskEntity(task.getName());
  }

  private TaskEntity getOrElseThrow(final String id) {
    try {
      return HibernateUtil.executeReadInTransaction(() -> fetch(toUUID(id)), em);
    } catch(NoResultException t) {
      throw new NotFoundException("task with id '" + id + "' is not found");
    }
  }

  private TaskEntity fetch(final UUID id) {
    return em.createQuery(FETCH_QUERY, entityClass)
        .setParameter(ID, id)
        .setLockMode(LockModeType.OPTIMISTIC)
        .getSingleResult();
  }

  private static Task toModel(TaskEntity taskEntity) {
    final Task task = new Task(taskEntity.getName(), taskEntity.getStatus());

    task.setId(HibernateUtil.toString(taskEntity.getId()));
    task.setAssigneeId(HibernateUtil.toString(taskEntity.getPerson().getId()));
    task.setBuildingId(HibernateUtil.toString(taskEntity.getBuilding().getId()));
    return task;
  }
}
