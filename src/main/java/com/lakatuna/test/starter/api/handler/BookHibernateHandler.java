package com.lakatuna.test.starter.api.handler;

import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.repository.BookHibernateRepository;
import com.lakatuna.test.starter.api.service.BookHibernateService;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.List;

public class BookHibernateHandler {

  private final BookHibernateService bookHibernateService;
  private final BookHibernateRepository bookHibernateRepository;

  private static final String ID_PARAMETER = "id";

  public BookHibernateHandler(BookHibernateService bookHibernateService, BookHibernateRepository bookHibernateRepository) {
    this.bookHibernateService = bookHibernateService;
    this.bookHibernateRepository = bookHibernateRepository;
  }

  public Uni<Book> findById(RoutingContext rc) {
    String id = rc.pathParam(ID_PARAMETER);
    return bookHibernateRepository.get(id);
  }

  public Uni<Book> create(RoutingContext rc) {
    Book book = rc.getBodyAsJson().mapTo(Book.class);
    return bookHibernateRepository.create(book);
  }

  public Uni<List<Book> > list(RoutingContext rc) {
    return bookHibernateRepository.list();
  }

  public Uni<Book> delete(RoutingContext rc) {
    String id = rc.pathParam(ID_PARAMETER);
    return bookHibernateRepository.delete(id);
  }

  public Uni<Book> update(RoutingContext rc) {
    Book book = rc.getBodyAsJson().mapTo(Book.class);
    return bookHibernateRepository.update(book);
  }



}
