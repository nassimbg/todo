package com.goldenrealstate.todo.data.client.hibernate;

import com.goldenrealstate.todo.data.client.NotFoundException;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.LockTimeoutException;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.PessimisticLockException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public final class HibernateUtil {

  private static final String PERSISTENCE_UNIT_NAME = "goldenRealStateToDo";

  private static final List<Class<? extends Throwable>> RETRY_ON_EXCEPTIONS = Arrays.asList(LockTimeoutException.class, OptimisticLockException.class, PessimisticLockException.class);
  /* Use a retry policy since we use optimistic locking for reading */
  private static final RetryPolicy<?> RETRY_POLICY = new RetryPolicy<>()
      .handle(RETRY_ON_EXCEPTIONS)
      .handleIf(t -> {
        if(t instanceof RollbackException) {
          final Throwable cause = t.getCause();
          return cause != null && RETRY_ON_EXCEPTIONS.contains(cause.getClass());
        }
        return false;
      })
      .withDelay(Duration.ofMillis(100))
      .withMaxRetries(4);

  private HibernateUtil() {
    //do nothing
  }

  public static <T> T executeReadInTransaction(Supplier<T> f, EntityManager em) {
    return executeInReliableTransaction(f, em);
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

  private static <T> T executeInReliableTransaction(Supplier<T> f, EntityManager em) {
    final boolean transactionAlreadyActive = onStartingTransaction(em);
    return Failsafe.with((RetryPolicy<T>) RETRY_POLICY)
        .onFailure(e -> onTransactionFailure(em, transactionAlreadyActive))
        .onSuccess(e -> onTransactionSuccess(em, transactionAlreadyActive))
        .get(() -> execute(f));
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

  private static <T> T execute(Supplier<T> f) {
    return f.get();
  }

  static <T> T findOrElseThrow(EntityManager em, Class<T> entityClass, final String id, Function<String, String> errorMsg) throws NotFoundException {
    final T entity = HibernateUtil.executeReadInTransaction(() -> em.find(entityClass, toUUID(id), LockModeType.OPTIMISTIC), em);

    if (entity == null) {
      throw new NotFoundException(errorMsg.apply(id));
    }
    return entity;
  }

  static UUID toUUID(String id) {
    return UUID.fromString(id);
  }

  static String toString(UUID id) {
    return id.toString();
  }

  static <T> TypedQuery<T> getAllQuery(Class<T> clazz, final EntityManager em) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> cq = cb.createQuery(clazz);
    Root<T> rootEntry = cq.from(clazz);
    CriteriaQuery<T> query = cq.select(rootEntry);

    return em.createQuery(query);
  }

  static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}
