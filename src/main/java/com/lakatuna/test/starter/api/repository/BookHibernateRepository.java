package com.lakatuna.test.starter.api.repository;

import com.lakatuna.test.starter.api.model.Book;
import io.vertx.core.json.JsonArray;
import org.hibernate.reactive.mutiny.Mutiny;

public class BookHibernateRepository extends AbstractRepository<Book, String> {

  public BookHibernateRepository(Class<Book> clazz, Mutiny.SessionFactory sessionFactory) {
    super(clazz, sessionFactory);
  }

  @Override
  protected String getDefaultOrderBy() {
    return null;
  }

  @Override
  public String getUpdateQuery(Book object) {
    return null;
  }

  @Override
  public JsonArray getUpdateJsonArray(Book object) {
    return null;
  }

  @Override
  public String getInsertQueryWithParams(Book book) {
    JsonArray jsonArray = new JsonArray();
    StringBuffer sb = new StringBuffer();
    StringBuffer values = new StringBuffer();
    jsonArray.clear();
    jsonArray.add(book.getActive());
    sb.append(", active");
    values.append(", " + jsonArray.getBoolean(0) + "");
    if (book.getId() != null) {
      jsonArray.clear();
      jsonArray.add(book.getId());
      sb.append(", id");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getAuthor() != null) {
      jsonArray.clear();
      jsonArray.add(book.getAuthor());
      sb.append(", author");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getCountry() != null) {
      jsonArray.clear();
      jsonArray.add(book.getCountry());
      sb.append(", country");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getImageLink() != null) {
      jsonArray.clear();
      jsonArray.add(book.getImageLink());
      sb.append(", image_link");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getLanguage() != null) {
      jsonArray.clear();
      jsonArray.add(book.getLanguage());
      sb.append(", language");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getLink() != null) {
      jsonArray.clear();
      jsonArray.add(book.getLink());
      sb.append(", link");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getPages() != null) {
      jsonArray.clear();
      jsonArray.add(book.getPages());
      sb.append(", pages");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getTitle() != null) {
      jsonArray.clear();
      jsonArray.add(book.getTitle());
      sb.append(", title");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    if (book.getYear() != null) {
      jsonArray.clear();
      jsonArray.add(book.getImageLink());
      sb.append(", image_link");
      values.append(", '" + jsonArray.getString(0) + "'");
    }
    return new StringBuffer(insertNative(Book.class)).append(sb.substring(1))
      .append(" )")
      .append(valuesNative())
      .append(values.substring(1))
      .append(")")
      .toString();
  }

}
