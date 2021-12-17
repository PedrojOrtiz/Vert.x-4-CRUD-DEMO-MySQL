package com.lakatuna.test.starter.api.handler;

import com.lakatuna.test.starter.api.model.Book;
import com.lakatuna.test.starter.api.model.BookGetAllResponse;
import com.lakatuna.test.starter.api.repository.BookHibernateRepository;
import com.lakatuna.test.starter.api.service.BookHibernateService;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.RoutingContext;

import java.util.List;
//import io.vertx.ext.web.RoutingContext;

public class BookHibernateHandler {

  private final BookHibernateService bookHibernateService;
  private final BookHibernateRepository bookHibernateRepository;

  public BookHibernateHandler(BookHibernateService bookHibernateService, BookHibernateRepository bookHibernateRepository) {
    this.bookHibernateService = bookHibernateService;
    this.bookHibernateRepository = bookHibernateRepository;
  }


//  public Uni<BookGetAllResponse> readAll(RoutingContext rc) {
//    final String page = rc.queryParams().get(PAGE_PARAMETER);
//    final String limit = rc.queryParams().get(LIMIT_PARAMETER);
//
//    return bookHibernateService.readAll(page, limit);
//      //.onFailure(throwable -> ResponseUtils.buildErrorResponse(rc, throwable.));
//  }

  public Uni<Book> findById(RoutingContext rc) {
    return bookHibernateRepository.get(rc);
  }

  public Uni<Book> create(RoutingContext rc) {
    return bookHibernateRepository.create(rc);
  }

  public Uni<List<Book> > list(RoutingContext rc) {
    return bookHibernateRepository.list(rc);
  }

}
