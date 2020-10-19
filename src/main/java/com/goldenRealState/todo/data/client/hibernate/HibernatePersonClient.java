package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.data.client.PersonClient;
import com.goldenrealstate.todo.data.model.hibernate.PersonEntity;
import com.goldenrealstate.todo.webapp.models.person.Person;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.executeWriteInTransaction;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.findOrElseThrow;
import static com.goldenrealstate.todo.data.client.hibernate.HibernateUtil.getAllQuery;

public class HibernatePersonClient implements PersonClient {

  private static final Function<String, String> NOT_FOUND_ERROR_MSG = id -> "Person with id '" + id + "' is not found";

  private final EntityManager em;
  private final Class<PersonEntity> entityClass;

  private HibernatePersonClient(EntityManager em) {
    this.em = em;
    this.entityClass = PersonEntity.class;
  }

  public static HibernatePersonClient create(EntityManager session) {
    return new HibernatePersonClient(session);
  }

  @Override
  public String post(final Person ob) {
    final PersonEntity personHibernate = toHibernate(ob);

    return executeWriteInTransaction(() -> {
      em.persist(personHibernate);

      return personHibernate.getId().toString();
    }, em);
  }

  @Override
  public Person get(final String id) throws NotFoundException {
    return toModel(findOrElseThrow(em, entityClass, id, NOT_FOUND_ERROR_MSG));
  }

  @Override
  public Collection<Person> getAll() {
    TypedQuery<PersonEntity> allQuery = getAllQuery(PersonEntity.class, em);
    return allQuery.getResultList()
        .stream()
        .map(HibernatePersonClient::toModel)
        .collect(Collectors.toList());
  }

  private static PersonEntity toHibernate(Person person) {
    return new PersonEntity(person.getName());
  }

  private static Person toModel(PersonEntity personHibernate) {
    final Person person = new Person(personHibernate.getName());

    person.setId(HibernateUtil.toString(personHibernate.getId()));
    return person;
  }
}
