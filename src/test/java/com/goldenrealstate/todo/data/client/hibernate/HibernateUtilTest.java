package com.goldenrealstate.todo.data.client.hibernate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HibernateUtilTest {

  private EntityManagerFactory entityManagerFactory;

  @AfterEach
  public void after() {
    HibernateUtil.reset(entityManagerFactory);
  }

  @Test
  void should_take_javax_persistence_url_from_config() {
    entityManagerFactory = HibernateUtil.getSessionJavaConfigFactory();

    final Object url = entityManagerFactory.getProperties().get("javax.persistence.jdbc.url");

    assertEquals("jdbc:postgresql://localhost/tests", url);
  }
}
