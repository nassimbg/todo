package com.goldenrealstate.todo.data.client.hibernate;

import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class Utils {

  public static void withEntityManager(Consumer<EntityManager> cons) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("dao_test", System.getProperties());

    try {
      cons.accept(emf.createEntityManager());
    } finally {
      emf.close();
    }
  }
}
