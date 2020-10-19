package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.person.Person;

import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HibernatePersonClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    Utils.withEntityManager(em -> {
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      postPersonAndAssert(hibernatePersonClient, "person 1");
    });
  }

  @Test
  void shouldPersistAndGetPerson() {
    Utils.withEntityManager(em -> {
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = postPersonAndAssert(hibernatePersonClient, "person 1");

      final Person retrievedPerson = hibernatePersonClient.get(person.getId());
      assertEquals(person, retrievedPerson);
    });
  }

  @Test
  void shouldFailSinceNotExistingPerson() {
    assertThrows(NotFoundException.class, () -> {
      Utils.withEntityManager(em -> {
        final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);

        hibernatePersonClient.get("0e3cf451-602f-4c18-9832-1f20399ee1cd");
      });
    });
  }

  @Test
  void shouldPersistAndGetAllPersons() {
    Utils.withEntityManager(em -> {
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = postPersonAndAssert(hibernatePersonClient, "person 1");
      final Person person2 = postPersonAndAssert(hibernatePersonClient, "person 2");

      final Collection<Person> retrievedPersons = hibernatePersonClient.getAll();
      assertEquals(2, retrievedPersons.size());
      assertTrue(retrievedPersons.contains(person));
      assertTrue(retrievedPersons.contains(person2));
    });
  }

  private Person postPersonAndAssert(final HibernatePersonClient hibernatePersonClient, String name) {
    final Person person = new Person(name);
    final String id = hibernatePersonClient.post(person);

    assertNotNull(id);
    person.setId(id);
    return person;
  }
}
