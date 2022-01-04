package com.lakatuna.test.starter.api.repository;

import com.lakatuna.test.starter.verticle.HibernateVerticle;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;


/**
 * @param <T>
 * @author fiorenzo pizza
 * <p/>
 * Class used to manage filters and ordering for lists
 * <p/>
 * The defaultOrder property is used to store the name of the field to
 * be used as a default for ordering
 * <p/>
 * The order property contains the actual field used for ordering
 */
public class Search<T> {

  private static final Logger logger = LoggerFactory.getLogger(Search.class);

  public T obj;
  public T like;
  public T not;
  public T from;
  public T to;
  public String track;

  public Class<T> classType;

  // --- to repository_create query
  public String query = null;
  public JsonArray jsonArray = null;
  public int startRow;
  public int pageSize;
  // --------- Ordering ----------------------------------------

  public String defaultOrder;
  public String order;
  public boolean orderAsc = true;
  // --------- Service ----------------------------------------

  /**
   * @param t
   */
  public Search(Class<T> t) {
    classType = t;
    this.obj = init(t);
    this.like = init(t);
    this.not = init(t);
    this.from = init(t);
    this.to = init(t);
    this.pageSize = 10;
    this.startRow = 0;
  }

  /**
   * @param o
   */
  public Search(T o) {
    this.obj = o;
  }

  /**
   * @param t
   * @return
   */
  private T init(Class<T> t) {
    try {
      return t.newInstance();
    } catch (InstantiationException e) {
      logger.error("init ", e);
      return null;
    } catch (IllegalAccessException e) {
      logger.error("init ", e);
      return null;
    }
  }
  // --------- Clear ----------------------------------------

  /**
   * Clear the active filters but not the ordering settings
   */
  public void clear() {
    this.not = init(classType);
    this.like = init(classType);
    this.obj = init(classType);
    this.from = init(classType);
    this.to = init(classType);
    this.pageSize = 10;
    this.startRow = 0;
  }

  /**
   * Clear the active filters and the the ordering settings
   */
  public void clearAll() {
    clear();
    this.order = this.defaultOrder;
    this.orderAsc = true;
  }

  @Override
  public String toString() {
    return "Search{" +
      "obj=" +
      obj +
      ", track=" +
      track +
      ", like=" +
      like +
      ", not=" +
      not +
      ", from=" +
      from +
      ", to=" +
      to +
      ", classType=" +
      classType +
      ", query='" +
      query +
      '\'' +
      ", jsonArray=" +
      jsonArray +
      ", startRow=" +
      startRow +
      ", pageSize=" +
      pageSize +
      ", defaultOrder='" +
      defaultOrder +
      '\'' +
      ", order='" +
      order +
      '\'' +
      ", orderAsc=" +
      orderAsc +
      '}';
  }
}
