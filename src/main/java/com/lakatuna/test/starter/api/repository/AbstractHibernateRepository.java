package com.lakatuna.test.starter.api.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

public abstract class AbstractHibernateRepository<T, K> {

  private Mutiny.SessionFactory emf;
  protected final Class<T> clazz;

  public AbstractHibernateRepository(Class<T> clazz, Mutiny.SessionFactory sessionFactory) {
    this.emf = sessionFactory;
    this.clazz = clazz;
  }

  public Uni<List<T>> findAll() {
    return this.emf.withSession(session -> session.find(clazz));
  }

  public Uni<List<T>> findAllByLimitOffset(int limit, int offset) {
    return this.emf.withSession(session -> session
      .createQuery("FROM ?1", clazz)
      .setParameter(1, clazz.getName())
      .setMaxResults(limit)
      .setFirstResult(offset)
      .getResultList());
  }

  public Uni<T> findById(K key) {
    System.out.println("emf ===> " + emf.toString());
    return this.emf.withSession(session -> session.find(clazz, key));
  }


  public Uni<Void> add(final T entity) {
    return this.emf.withTransaction((session, tx) -> session.persist(entity));
  }

  public Uni<T> delete(final K key) {
    return this.emf.withSession(session -> session.find(clazz, key))
      .onItem()
      .invoke(entity -> emf.withTransaction((session, tx) -> session.remove(entity)));
  }

  public Uni<T> update(final T entity) {
    return this.emf.withTransaction((session, tx) -> session.merge(entity));
  }

  public Uni<Integer> count(Class<T> classType) {
    System.out.println("Count From ======> " + classType.getSimpleName());
    return this.emf.withSession(session -> session
      .createNativeQuery("SELECT COUNT(*) AS total FROM ".concat(classType.getSimpleName()))
      .getSingleResult().map(total -> (Integer) total ));
  }

}
