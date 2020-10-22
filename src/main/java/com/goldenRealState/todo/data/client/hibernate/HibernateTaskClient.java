package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.TaskClient;
import com.goldenrealstate.todo.data.model.hibernate.BuildingEntity;
import com.goldenrealstate.todo.data.model.hibernate.PersonEntity;
import com.goldenrealstate.todo.data.model.hibernate.TaskEntity;
import com.goldenrealstate.todo.webapp.models.task.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.isNullOrEmpty;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.toUUID;

public final class HibernateTaskClient implements TaskClient {

  private static final String ID = "id";
  private static final String PERSON_REF_FIELD = "person";
  private static final String BUILDING_REF_FIELD = "building";

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
    return writeTask(ob, em::persist);
  }

  @Override
  public Task get(final String id) throws NotFoundException {
    return toModel(getOrElseThrow(id));
  }

  @Override
  public Collection<Task> getAll() {
    return getAll(null, null);
  }

  @Override
  public Collection<Task> getAll(final String assigneeId, final String buildingId) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<TaskEntity> cq = cb.createQuery(entityClass);
    Root<TaskEntity> rootEntry = cq.from(entityClass);

    List<Predicate> conditions = new ArrayList<>();
    createJoinOnIdCondition(assigneeId, cb, rootEntry, conditions, PERSON_REF_FIELD);
    createJoinOnIdCondition(buildingId, cb, rootEntry, conditions, BUILDING_REF_FIELD);

    CriteriaQuery<TaskEntity> query = cq.select(rootEntry);

    if(!conditions.isEmpty()) {
      query = query.where(conditions.toArray(new Predicate[0]));
    }

    return toModel(em.createQuery(query));
  }

  @Override
  public void put(final String id, final Task task) {
    task.setId(id);
    writeTask(task, taskEntity -> {
      taskEntity.setId(toUUID(id));

      final TaskEntity persistedEntity = getOrElseThrow(id);
      taskEntity.setVersion(persistedEntity.getVersion());
      em.merge(taskEntity);
    });
  }

  private String writeTask(final Task task, Consumer<TaskEntity> writer) {
    return executeWriteInTransaction(() -> {
      final TaskEntity taskEntity = prepareEntity(task);

      writer.accept(taskEntity);

      return taskEntity.getId().toString();
    }, em);
  }

  private static void createJoinOnIdCondition(final String id, final CriteriaBuilder cb,
      final Root<TaskEntity> rootEntry, final List<Predicate> conditions, final String fieldName) {
    if (!isNullOrEmpty(id)) {
      final Join personJoin = Join.class.cast(rootEntry.fetch(fieldName));

      conditions.add(cb.equal(personJoin.get(HibernateTaskClient.ID), toUUID(id)));
    }
  }

  private List<Task> toModel(final TypedQuery<TaskEntity> allQuery) {
    return allQuery.getResultList()
        .stream()
        .map(HibernateTaskClient::toModel)
        .collect(Collectors.toList());
  }

  private static TaskEntity toHibernate(Task task) {
    final TaskEntity taskEntity = new TaskEntity(task.getName());
    taskEntity.setStatus(task.getStatus());
    return taskEntity;
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

  private TaskEntity prepareEntity(final Task ob) {
    final TaskEntity taskEntity = toHibernate(ob);
    final PersonEntity assignee = em.getReference(PersonEntity.class, toUUID(ob.getAssigneeId()));
    final BuildingEntity assignedBuilding = em.getReference(BuildingEntity.class, toUUID(ob.getBuildingId()));

    taskEntity.setBuilding(assignedBuilding);
    taskEntity.setPerson(assignee);
    return taskEntity;
  }

  private static Task toModel(TaskEntity taskEntity) {
    final Task task = new Task(taskEntity.getName(), taskEntity.getStatus());

    task.setId(HibernateUtil.toString(taskEntity.getId()));
    task.setAssigneeId(HibernateUtil.toString(taskEntity.getPerson().getId()));
    task.setBuildingId(HibernateUtil.toString(taskEntity.getBuilding().getId()));
    return task;
  }
}
