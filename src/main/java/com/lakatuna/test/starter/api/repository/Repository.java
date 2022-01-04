package com.lakatuna.test.starter.api.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.List;

/**
 * Created by Pedro on 21/12/2021.
 */
public interface Repository<T, K> {

  Uni<List<T>> list();

  Uni<T> get(K id);

  Uni<T> create(T t);

  Uni<T> update(T t);

  Uni<T> delete(K id);

  String insertNative(Class<? extends Object> clazz);

  String updateNative(Class<? extends Object> clazz);

  Uni<Object> createNative(T t);

  String whereNative();

  String valuesNative();

  void createNative(T t, Handler<AsyncResult<Object>> handler);

}
