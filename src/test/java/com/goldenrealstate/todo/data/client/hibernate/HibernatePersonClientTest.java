package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;
import com.goldenrealstate.todo.webapp.models.person.Person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HibernatePersonClientTest {

  @Test
  void shouldPersistAndReturnGeneratedId() {
    Utils.withEntityManager(em -> {
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = new Person("person 1");

      final String post = hibernatePersonClient.post(person);

      assertNotNull(post);
    });
  }

  @Test
  void shouldPersistAndGetPerson() {
    Utils.withEntityManager(em -> {
      final HibernatePersonClient hibernatePersonClient = HibernatePersonClient.create(em);
      final Person person = new Person("person 1");

      final String id = hibernatePersonClient.post(person);

      assertNotNull(id);
      person.setId(id);

      final Person retrievedPerson = hibernatePersonClient.get(id);
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
}
