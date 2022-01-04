package com.lakatuna.test.starter.api.repository;

import com.lakatuna.test.starter.utils.RepositoryUtils;
import io.smallrye.mutiny.Uni;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;
import io.vertx.mutiny.core.Promise;
import org.hibernate.reactive.mutiny.Mutiny;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by Pedro on 23/12/21.
 */
public abstract class AbstractRepository<T, K> implements Repository<T, K> {

  private static final Logger logger = LoggerFactory.getLogger(AbstractRepository.class);

  private final Class<T> entityClass;
  protected Mutiny.SessionFactory emf;

  //FOR NATIVE QUERYS
  String INSERT = "INSERT INTO %s ( ";
  String UPDATE = "UPDATE %s SET ";
  String WHERE = "  WHERE %s = ? ";
  String VALUES = " VALUES( ";

  public AbstractRepository(Class<T> entityClass, Mutiny.SessionFactory emf) {
    this.entityClass = entityClass;
    this.emf = emf;
  }

  /**
   * BASIC HIBERNATE CRUD METHODS
   **/

  @Override
  public Uni<List<T>> list() {
    String tableName = "";
    try {
      tableName = RepositoryUtils.getTableName(entityClass);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String finalTableName = tableName;
    return emf.withSession(session -> session
      .createQuery("FROM ".concat(finalTableName), entityClass)
      .getResultList());
  }

  @Override
  public Uni<T> get(K id) {
    return this.emf.withSession(session -> session
      .find(this.entityClass, id));
  }

  @Override
  public Uni<T> create(T t) {
    return emf.withSession(session -> session
      .persist(t)
      .call(session::flush)
      .replaceWith(t));
  }

  @Override
  public Uni<T> update(T t) {
    return emf.withSession(session -> session
      .merge(t)
      .call(session::flush)
      .replaceWith(t));
  }

  @Override
  public Uni<T> delete(K id) {

    return emf.withTransaction(
      (session, transaction) -> session
        .find(this.entityClass, id)
        .chain(t -> session
          .remove(t)
          .call(session::flush)
          .replaceWith(t))
    );

  }

  /**
   * NATIVE QUERIES METHODS
   * INSERT Native methods can be replaced by Hibernate methods
   * LIST Native methods have to be refactored
   **/

  @Override
  public String insertNative(Class<? extends Object> clazz) {
    String tableName = table(clazz);
    return String.format(INSERT, tableName);
  }

  @Override
  public String updateNative(Class<? extends Object> clazz) {
    String tableName = table(clazz);
    return String.format(UPDATE, tableName);
  }

  @Override
  public String whereNative() {
    String id = null;
    try {
      id = RepositoryUtils.getIdFieldName(getEntityType());
    } catch (Exception e) {
    }
    return String.format(WHERE, id);
  }

  @Override
  public String valuesNative() {
    return VALUES;
  }

  @Override
  public Uni<Object> createNative(T t) {
    logger.info("repository_create_native t:");
    Promise<Object> updateResultPromise = Promise.promise();
    createNative(t, updateResultPromise.getDelegate());
    return updateResultPromise.future();
  }

  @Override
  public void createNative(T t, Handler<AsyncResult<Object>> handler) {
    String query = getInsertQueryWithParams(t);
    emf.withSession(session -> {
      Promise<Uni<Object> > updateResultPromise = Promise.promise();
      updateResultPromise.complete(session.createNativeQuery(query).getSingleResult());
      handler.handle((AsyncResult<Object>) updateResultPromise.future());
      return null;
    });
  }


  /**
   * Get Result Set from built custom SELECT query
   *
   * @param search
   * @return
   */
  public Uni<Uni<List<Object>> > listNative(Search<T> search) {

    Promise<Uni<List<Object>> > uniPromise = Promise.promise();

    listNative(search, uniPromise.getDelegate());

    return uniPromise.future();

  }

  /**
   * @param search
   * @param handler
   */
  public void listNative(Search<T> search, Handler<AsyncResult<ResultSet>> handler) {

    emf.withSession(session -> {
      try {
        getRestrictions(search, false);
      } catch (Exception e) {
        logger.info("Could not get Restriction");
      }
      logger.info("[" + search.track + "] - list:" + search.query + " - jsonArray:" + search.jsonArray);
      return session.createNativeQuery(search.query).getResultList();
    });

  }


  /**
   * @param search:    object where a custom native SELECT query is build
   * @param justCount: boolean for SELECT: count(*) or *
   * @throws Exception
   */
  protected void getRestrictions(Search<T> search, boolean justCount) throws Exception {
    search.jsonArray = new JsonArray();
    String alias = "c";
    StringBuffer sb = new StringBuffer(getBaseList(search.obj.getClass(), alias, justCount));
    String separator = " where ";
    if (!justCount) {
      sb.append(getOrderBy(alias, search.order));
      sb.append(getLimit(search));

    }
    search.query = sb.toString();
    logger.info("[" + search.track + "] - query:" + search.query + " - jsonArray:" + search.jsonArray);
  }

  protected String getBaseList(Class<? extends Object> clazz, String alias, boolean count) throws Exception {
    String tableName = table(clazz);
    if (count) {
      return "select count(*) from " + tableName + " " + alias + " ";
    } else {
      return "select * from " + tableName + " " + alias + " ";
    }
  }

  /**
   * Abstract Methods to be implemented as needed
   */
  protected abstract String getDefaultOrderBy();

  public abstract String getInsertQueryWithParams(T object);

  public abstract String getUpdateQuery(T object);

  public abstract JsonArray getUpdateJsonArray(T object);


  protected Class<T> getEntityType() throws Exception {
    ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
    return (Class<T>) parameterizedType.getActualTypeArguments()[0];
  }

  public String getOrderBy(String alias, String orderBy) throws Exception {
    try {
      if (orderBy == null || orderBy.length() == 0) {
        orderBy = getDefaultOrderBy();
      }
      StringBuffer result = new StringBuffer();
      String[] orders = orderBy.split(",");
      for (String order : orders) {
        result.append(", ");
        if (alias != null && alias.trim().isEmpty()) {
          result.append(alias).append(".");
        }
        result.append(order.trim()).append(" ");
      }
      return " order by " + result.toString().substring(2);
    } catch (Exception e) {
      logger.error("getOrderBy ", e);
      return "";
    }
  }

  protected String getLimit(Search<T> search) {
    if (search.startRow == 0 && search.pageSize == 0) {
      return "";
    }
    if (search.startRow > 0) {
      return " limit " + search.pageSize + " OFFSET " + search.startRow;
    } else {
      return " limit " + search.pageSize;
    }
  }

  protected String table(Class<? extends Object> clazz) {
    String tableName = null;
    try {
      tableName = RepositoryUtils.getTableName(clazz);
      if (tableName == null) {
        tableName = clazz.getSimpleName();
      }
    } catch (Exception e) {
    }
    return tableName;
  }


}
