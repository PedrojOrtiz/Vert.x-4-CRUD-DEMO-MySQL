package com.lakatuna.test.starter.api.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

//implements ManagementClass<T>

public abstract class AbstractHRepository<T,K>  {

  private final Class<T> entityClass;

  private static final String ID_PARAMETER = "id";

  protected Mutiny.SessionFactory emf;

  public AbstractHRepository(Class<T> entityClass, Mutiny.SessionFactory emf) {
    this.entityClass = entityClass;
    this.emf = emf;
  }

  // tag::crud-methods[]
  public Uni<List<T>> list(RoutingContext ctx) {
    System.out.println("getting.");
    return emf.withSession(session -> session
      .createQuery("FROM ".concat(this.entityClass.getSimpleName()), entityClass)
//      .setParameter(1, this.classType.getSimpleName())
      .getResultList());
  }

  public Uni<T> get(RoutingContext ctx) {
    K id = (K) ctx.pathParam(ID_PARAMETER);
    return this.emf.withSession(session -> session
      .find(this.entityClass, id));
  }

  public Uni<T> create(RoutingContext ctx) {
    T t = ctx.getBodyAsJson().mapTo(this.entityClass);
    return emf.withSession(session -> session
      .persist(t)
      .call(session::flush)
      .replaceWith(t));
  }

  public Uni<T> update(RoutingContext ctx) {
    T t = ctx.getBodyAsJson().mapTo(this.entityClass);
    return emf.withSession(session -> session
      .merge(t)
      .call(session::flush)
      .replaceWith(t));
  }

  public Uni<T> delete(RoutingContext ctx) {

    K id = (K) ctx.pathParam(ID_PARAMETER);

    return emf.withTransaction(
      (session, transaction) -> session.find(this.entityClass, id)
        .chain(t -> session
          .remove(t)
          .call(session::flush)
          .replaceWith(t) )
    );

  }
  // end::crud-methods[]



}
