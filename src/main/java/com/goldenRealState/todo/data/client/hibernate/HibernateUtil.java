package com.goldenrealstate.todo.data.client.hibernate;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class HibernateUtil {

  private static final String PERSISTENCE_UNIT_NAME = "goldenRealStateToDo";

  private HibernateUtil() {
    //do nothing
  }

  public static <T> T executeWriteInTransaction(Supplier<T> f, EntityManager em) {
    boolean transactionAlreadyActive = onStartingTransaction(em);
    try {
      T t = f.get();
      onTransactionSuccess(em, transactionAlreadyActive);
      return t;
    } catch (Throwable t) {
      onTransactionFailure(em, transactionAlreadyActive);
      throw t;
    }
  }

  private static boolean onStartingTransaction(EntityManager em) {
    final boolean transactionAlreadyActive = em.getTransaction().isActive();
    if (!transactionAlreadyActive) {
      em.getTransaction().begin();
    }
    return transactionAlreadyActive;
  }

  private static void onTransactionFailure(EntityManager em, boolean transactionAlreadyActive) {
    if (!transactionAlreadyActive) {
      em.getTransaction().rollback();
    }
  }

  private static void onTransactionSuccess(EntityManager em, boolean transactionAlreadyActive) {
    if (!transactionAlreadyActive && !em.getTransaction().getRollbackOnly()) {
      em.getTransaction().commit();
    }
    if (!transactionAlreadyActive && em.getTransaction().getRollbackOnly()) {
      em.getTransaction().rollback();
    }
  }

  public static void reset(final EntityManagerFactory entityManagerFactory) {
    if (entityManagerFactory != null) {
      entityManagerFactory.close();
    }
  }

  public static EntityManagerFactory getSessionJavaConfigFactory() {
    try {
      return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    } catch (Throwable ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }
}
